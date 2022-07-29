package com.hitqz.disinfectionrobot.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hitqz.disinfectionrobot.constant.Constants;
import com.hitqz.disinfectionrobot.constant.TokenKeys;
import com.hitqz.disinfectionrobot.data.MapBuildRequest;
import com.hitqz.disinfectionrobot.data.MapDataGetResponse;
import com.hitqz.disinfectionrobot.data.MapUploadRequest;
import com.hitqz.disinfectionrobot.databinding.ActivityBuildMapBinding;
import com.hitqz.disinfectionrobot.dialog.CommonDialog;
import com.hitqz.disinfectionrobot.dialog.SaveMapDialog;
import com.hitqz.disinfectionrobot.net.BaseDataObserver;
import com.hitqz.disinfectionrobot.net.ws.JWebSocketClient;
import com.hitqz.disinfectionrobot.net.ws.JWebSocketClientService;
import com.sonicers.commonlib.rx.RxSchedulers;

@SuppressLint("CheckResult")
public class BuildMapActivity extends BaseActivity {
    public static final String TAG = BuildMapActivity.class.getSimpleName();

    ActivityBuildMapBinding mBinding;
    private boolean mBuildingMap = false;
    private ChatMessageReceiver chatMessageReceiver;
    private JWebSocketClient client2;
    private JWebSocketClientService.JWebSocketClientBinder binder;
    private JWebSocketClientService jWebSClientService;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.e("MainActivity", "服务与活动成功绑定");
            binder = (JWebSocketClientService.JWebSocketClientBinder) iBinder;
            jWebSClientService = binder.getService();
            jWebSClientService.mOnClintOpenListener = new JWebSocketClientService.onClintOpenListener() {
                @Override
                public void onClientOpen(String url) {
                    if (Constants.WS_MAP_BUILD.equals(url)) {
                        client2 = jWebSClientService.client2;
                    }
                }
            };
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.e("MainActivity", "服务与活动成功断开");
        }
    };

    /**
     * 绑定服务
     */
    private void bindService() {
        Intent bindIntent = new Intent(this, JWebSocketClientService.class);
        bindService(bindIntent, serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityBuildMapBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        setListener();
        mBinding.btnMapBuild.setText(mBuildingMap ? "保存地图" : "开始建图");
        bindService();
        doRegisterReceiver();
    }

    private void setListener() {

        mBinding.btnMapBuild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBuildingMap) {
                    SaveMapDialog dialog = new SaveMapDialog();
                    dialog.setOnClickListener(new SaveMapDialog.OnClickListener() {
                        @Override
                        public void onConfirm(String text) {
                            showDialog();
                            String token = SPUtils.getInstance().getString(TokenKeys.token);
                            MapUploadRequest request = new MapUploadRequest(token, text);
                            mISkyNet.map_upload(request).compose(RxSchedulers.io_main())
                                    .subscribeWith(new BaseDataObserver<Object>() {
                                        @Override
                                        public void onSuccess(Object model) {
                                            dismissDialog();
                                            ToastUtils.showShort("保存地图成功");
                                        }

                                        @Override
                                        public void onFailure(String msg) {
                                            dismissDialog();
                                            ToastUtils.showShort("保存地图失败:" + msg);
                                        }
                                    });
                        }
                    });
                    dialog.show(getSupportFragmentManager(), SaveMapDialog.TAG);
                } else {
                    if (client2 != null && client2.isOpen()) {
                        String token = SPUtils.getInstance().getString(TokenKeys.token);
                        jWebSClientService.sendMsg2("{token:\"" + token + "}");
                    } else {
                        ToastUtils.showShort("连接已断开，请稍等或重启App哟");
                        return;
                    }
                    showDialog();
                    String token = SPUtils.getInstance().getString(TokenKeys.token);
                    MapBuildRequest request = new MapBuildRequest(token, 0);

                    mISkyNet.map_build(request).compose(RxSchedulers.io_main())
                            .subscribeWith(new BaseDataObserver<Object>() {
                                @Override
                                public void onSuccess(Object model) {
                                    dismissDialog();
                                    ToastUtils.showShort("开始建图成功");
                                }

                                @Override
                                public void onFailure(String msg) {
                                    dismissDialog();
                                    ToastUtils.showShort("开始建图失败:" + msg);
                                }
                            });
                }
            }
        });
        mBinding.includeLayoutCommonTitleBar.ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        CommonDialog dialog = new CommonDialog();
        dialog.setOnClickListener(new CommonDialog.OnClickListener() {
            @Override
            public void onConfirm() {
                finish();
            }
        });
        dialog.show(getSupportFragmentManager(), dialog.getTag());
    }

    /**
     * 动态注册广播
     */
    private void doRegisterReceiver() {
        chatMessageReceiver = new ChatMessageReceiver();
        IntentFilter filter = new IntentFilter("com.hitqz.ws.content");
        registerReceiver(chatMessageReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(chatMessageReceiver);
    }

    private class ChatMessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            Log.d(TAG, "收到：" + message);
            MapDataGetResponse mapDataGetResponse = null;
            try {
                mapDataGetResponse = GsonUtils.fromJson(message, MapDataGetResponse.class);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (mapDataGetResponse == null || mapDataGetResponse.getMapData() == null) {
                return;
            }

            mBinding.bmv.setBuildNow(true);
//            if (mBinding.bmv.isBuildNow()) {
//                mBinding.bmv.setMapData(mapDataGetResponse.getMapData());
//                mBinding.bmv.postInvalidate();
//            }
        }
    }
}
