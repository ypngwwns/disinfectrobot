package com.hitqz.disinfectionrobot.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hitqz.disinfectionrobot.DisinfectRobotApplication;
import com.hitqz.disinfectionrobot.R;
import com.hitqz.disinfectionrobot.constant.TokenKeys;
import com.hitqz.disinfectionrobot.data.LoginRequest;
import com.hitqz.disinfectionrobot.data.LoginResponse;
import com.hitqz.disinfectionrobot.data.RobotStatus;
import com.hitqz.disinfectionrobot.databinding.ActivityMainBinding;
import com.hitqz.disinfectionrobot.fragment.DeployFragment;
import com.hitqz.disinfectionrobot.fragment.MainFragment;
import com.hitqz.disinfectionrobot.fragment.SettingFragment;
import com.hitqz.disinfectionrobot.i.IGo;
import com.hitqz.disinfectionrobot.net.BaseDataObserver;
import com.hitqz.disinfectionrobot.net.ws.JWebSocketClientService;
import com.sonicers.commonlib.rx.RxSchedulers;
import com.sonicers.commonlib.singleton.GsonUtil;

@SuppressLint("CheckResult")
public class MainActivity extends BaseActivity implements IGo {
    public static final String TAG = "MainActivity";

    ActivityMainBinding mBinding;
    private MainFragment mMainFragment;
    private DeployFragment mDeployFragment;
    private SettingFragment mSettingFragment;
    private int mBackPressCount = 0;
    private Handler mHandler;
    private WebSocketMessageReceiver mWebSocketMessageReceiver;

    /**
     * 动态注册广播
     */
    private void doRegisterReceiver() {
        mWebSocketMessageReceiver = new WebSocketMessageReceiver(this);
        DisinfectRobotApplication.instance.addWebSocketCallback(mWebSocketMessageReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        DisinfectRobotApplication.instance.removeWebSocketCallback(mWebSocketMessageReceiver);
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
        showDialog();
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.username = "admin";
        loginRequest.password = "hgd2022";

        mISkyNet.login(loginRequest).compose(RxSchedulers.io_main())
                .subscribeWith(new BaseDataObserver<LoginResponse>() {
                    @Override
                    public void onSuccess(LoginResponse model) {
                        dismissDialog();
                        ToastUtils.showShort("登录成功");
                        SPUtils.getInstance().put(TokenKeys.token, model.token);
                        go2Main();
                    }

                    @Override
                    public void onFailure(String msg) {
                        dismissDialog();
                        go2Main();
                        ToastUtils.showShort("登录失败");
                    }
                });

        setListener();
        doRegisterReceiver();
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

    private static class WebSocketMessageReceiver implements JWebSocketClientService.WebSocketCallback {
        private MainActivity mActivity;

        public WebSocketMessageReceiver(MainActivity activity) {
            mActivity = activity;
        }

        @Override
        public void onMessage(String message) {
            RobotStatus robotStatus = GsonUtil.getInstance().fromJson(message, RobotStatus.class);
            if (robotStatus == null || robotStatus.getPowerInfo() == null) {
                return;
            }

            if (mActivity.mMainFragment != null) {
                mActivity.mMainFragment.refresh(robotStatus);
            }
        }

        @Override
        public void onConnectSuccess(String s) {
            DisinfectRobotApplication.instance.jWebSClientService.sendMsg("{\"topic\": \"ROBOT_STATUS_GET\"}");
        }
    }
}