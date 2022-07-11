package com.hitqz.disinfectionrobot.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AppOpsManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentTransaction;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hitqz.disinfectionrobot.R;
import com.hitqz.disinfectionrobot.constant.TokenKeys;
import com.hitqz.disinfectionrobot.databinding.ActivityMainBinding;
import com.hitqz.disinfectionrobot.fragment.DeployFragment;
import com.hitqz.disinfectionrobot.fragment.MainFragment;
import com.hitqz.disinfectionrobot.fragment.SettingFragment;
import com.hitqz.disinfectionrobot.i.IGo;
import com.hitqz.disinfectionrobot.net.BaseDataObserver;
import com.hitqz.disinfectionrobot.net.data.UserLoginData;
import com.hitqz.disinfectionrobot.net.ws.JWebSocketClient;
import com.hitqz.disinfectionrobot.net.ws.JWebSocketClientService;
import com.hitqz.disinfectionrobot.util.MD5Util;
import com.sonicers.commonlib.rx.RxSchedulers;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@SuppressLint("CheckResult")
public class MainActivity extends BaseActivity implements IGo {
    public static final String TAG = "MainActivity";

    ActivityMainBinding mBinding;
    private MainFragment mMainFragment;
    private DeployFragment mDeployFragment;
    private SettingFragment mSettingFragment;
    private int mBackPressCount = 0;
    private Handler mHandler;

    private JWebSocketClient client;
    private JWebSocketClientService.JWebSocketClientBinder binder;
    private JWebSocketClientService jWebSClientService;
    private ChatMessageReceiver chatMessageReceiver;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.e("MainActivity", "服务与活动成功绑定");
            binder = (JWebSocketClientService.JWebSocketClientBinder) iBinder;
            jWebSClientService = binder.getService();
            jWebSClientService.mOnClintOpenListener = new JWebSocketClientService.onClintOpenListener() {
                @Override
                public void onClientOpen(String url) {
                    client = jWebSClientService.client;
                    initWebSocket();
                }
            };
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.e("MainActivity", "服务与活动成功断开");
        }
    };

    private void initWebSocket() {
        if (client != null && client.isOpen()) {
            jWebSClientService.sendMsg("");
        } else {
            ToastUtils.showShort("连接已断开，请稍等或重启App哟");
        }
    }

    /**
     * 启动服务（websocket客户端服务）
     */
    private void startJWebSClientService() {
        Intent intent = new Intent(this, JWebSocketClientService.class);
        startService(intent);
    }

    /**
     * 动态注册广播
     */
    private void doRegisterReceiver() {
        chatMessageReceiver = new ChatMessageReceiver();
        IntentFilter filter = new IntentFilter("com.hitqz.ws.content.content");
        registerReceiver(chatMessageReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        unregisterReceiver(chatMessageReceiver);
    }

    /**
     * 绑定服务
     */
    private void bindService() {
        Intent bindIntent = new Intent(this, JWebSocketClientService.class);
        bindService(bindIntent, serviceConnection, BIND_AUTO_CREATE);
    }


    //动态访问权限弹窗
    public Boolean checkPermission() {
        boolean isGranted = true;
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            if (this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                isGranted = false;
            }
            if (this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                isGranted = false;
            }
            Log.i("读写权限获取", " ： " + isGranted);
            if (!isGranted) {
                this.requestPermissions(
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission
                                .ACCESS_FINE_LOCATION,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        102);
            }
        }
        return isGranted;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        checkPermission();
        mHandler = new Handler();
        String passwd = MD5Util.string2MD5("123456");
        showDialog();
        mISkyNet.oauthToken(
                        "web-app",
                        "123456",
                        "password",
                        "admin",
                        passwd,
                        null
                ).compose(RxSchedulers.io_main())
                .subscribeWith(new BaseDataObserver<UserLoginData>() {
                    @Override
                    public void onSuccess(UserLoginData model) {
                        dismissDialog();
                        ToastUtils.showShort("登录成功");
                        SPUtils.getInstance().put(
                                TokenKeys.expiresIn,
                                System.currentTimeMillis() + (model.getExpiresIn() - 10) * 1000
                        );
                        SPUtils.getInstance().put(TokenKeys.tokenHead, model.getTokenHead());
                        SPUtils.getInstance().put(TokenKeys.token, model.getToken());
                        go2Main();
                    }

                    @Override
                    public void onFailure(String msg) {
                        dismissDialog();
                        ToastUtils.showShort("登录失败");
                    }
                });

        setListener();

        startJWebSClientService();
        //绑定服务
        bindService();
        checkNotification(this);
        doRegisterReceiver();
    }

    /**
     * 获取通知权限,监测是否开启了系统通知
     *
     * @param context
     */
    private boolean isNotificationEnabled(Context context) {

        String CHECK_OP_NO_THROW = "checkOpNoThrow";
        String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;

        Class appOpsClass = null;
        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE,
                    String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);

            int value = (Integer) opPostNotificationValue.get(Integer.class);
            return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 检测是否开启通知
     *
     * @param context
     */
    private void checkNotification(final Context context) {
        if (!isNotificationEnabled(context)) {
            new AlertDialog.Builder(context).setTitle("温馨提示")
                    .setMessage("你还未开启系统通知，将影响消息的接收，要去开启吗？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setNotification(context);
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
        }
    }

    /**
     * 如果没有开启通知，跳转至设置界面
     *
     * @param context
     */
    private void setNotification(Context context) {
        Intent localIntent = new Intent();
        //直接跳转到应用通知设置的代码：
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            localIntent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            localIntent.putExtra("app_package", context.getPackageName());
            localIntent.putExtra("app_uid", context.getApplicationInfo().uid);
        } else if (android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            localIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            localIntent.addCategory(Intent.CATEGORY_DEFAULT);
            localIntent.setData(Uri.parse("package:" + context.getPackageName()));
        } else {
            //4.4以下没有从app跳转到应用通知设置页面的Action，可考虑跳转到应用详情页面,
            localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= 9) {
                localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
            } else if (Build.VERSION.SDK_INT <= 8) {
                localIntent.setAction(Intent.ACTION_VIEW);
                localIntent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails");
                localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
            }
        }
        context.startActivity(localIntent);
    }

    private void setListener() {
        mBinding.bottomNavigation.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.page_1) {
                    go2Main();
                } else if (item.getItemId() == R.id.page_2) {
                    go2Deploy();
                } else if (item.getItemId() == R.id.page_3) {
                    go2Setting();
                }
                return true;
            }
        });
    }

    @Override
    public void go2Main() {
        hideOther();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (mMainFragment == null) {
            mMainFragment = MainFragment.newInstance();
            fragmentTransaction.add(R.id.vp_content, mMainFragment);
        } else {
            fragmentTransaction.show(mMainFragment);
        }

        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void go2Deploy() {
        hideOther();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (mDeployFragment == null) {
            mDeployFragment = DeployFragment.newInstance();
            fragmentTransaction.add(R.id.vp_content, mDeployFragment);
        } else {
            fragmentTransaction.show(mDeployFragment);
        }

        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void go2Setting() {
        hideOther();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (mSettingFragment == null) {
            mSettingFragment = SettingFragment.newInstance();
            fragmentTransaction.add(R.id.vp_content, mSettingFragment);
        } else {
            fragmentTransaction.show(mSettingFragment);
        }

        fragmentTransaction.commitAllowingStateLoss();
    }

    private void hideOther() {
        if (mMainFragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.hide(mMainFragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
        if (mDeployFragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.hide(mDeployFragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
        if (mSettingFragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.hide(mSettingFragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    @Override
    public void onBackPressed() {
        mBackPressCount++;
        if (mBackPressCount == 1) {
            ToastUtils.showShort("再按一次退出");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBackPressCount = 0;
                }
            }, 1500); // 延时1.5秒清空
        } else if (mBackPressCount >= 2) {
            finish();
        }
    }

    private class ChatMessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            Log.d(TAG, "收到：" + message);
        }
    }
}