package com.hitqz.disinfectionrobot.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.hitqz.disinfectionrobot.constant.Constants;
import com.hitqz.disinfectionrobot.data.MapCode;
import com.hitqz.disinfectionrobot.data.RobotoCreateMapIncrementDataDto;
import com.hitqz.disinfectionrobot.databinding.ActivityBuildMapBinding;
import com.hitqz.disinfectionrobot.dialog.CommonDialog;
import com.hitqz.disinfectionrobot.dialog.SaveMapDialog;
import com.hitqz.disinfectionrobot.net.BaseDataObserver;
import com.sonicers.commonlib.rx.RxSchedulers;
import com.sonicers.commonlib.singleton.GsonUtil;

@SuppressLint("CheckResult")
public class BuildMapActivity extends BaseActivity {
    public static final String TAG = BuildMapActivity.class.getSimpleName();

    ActivityBuildMapBinding mBinding;
    private WebSocketMessageReceiver mWebSocketMessageReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityBuildMapBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        setListener();
        mBinding.btnMapBuild.setText("开始建图");
        doRegisterReceiver();
    }

    private void setListener() {

        mBinding.btnMapBuild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("保存地图".contentEquals(mBinding.btnMapBuild.getText())) {
                    SaveMapDialog dialog = new SaveMapDialog();
                    dialog.setOnClickListener(new SaveMapDialog.OnClickListener() {
                        @Override
                        public void onConfirm(String text) {
                            showDialog();
                            MapCode mapCode = new MapCode();
                            mapCode.mapCode = text;
                            mISkyNet.finishBuildMap(mapCode).compose(RxSchedulers.io_main())
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
                    showDialog();
                    mISkyNet.buildMap().compose(RxSchedulers.io_main())
                            .subscribeWith(new BaseDataObserver<Object>() {
                                @Override
                                public void onSuccess(Object model) {
                                    mBinding.btnMapBuild.setText("保存地图");
                                    dismissDialog();
                                    ToastUtils.showShort("开始建图成功");
                                }

                                @Override
                                public void onFailure(String msg) {
                                    dismissDialog();
                                    ToastUtils.showShort("开始建图失败:%s", msg);
                                }
                            });
                }
            }
        });
        mBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonDialog dialog = new CommonDialog();
                dialog.setOnClickListener(new CommonDialog.OnClickListener() {
                    @Override
                    public void onConfirm() {

                        mISkyNet.cancelBuildMap().compose(RxSchedulers.io_main())
                                .subscribeWith(new BaseDataObserver<Object>() {
                                    @Override
                                    public void onSuccess(Object model) {
                                        mBinding.btnMapBuild.setText("开始建图");
                                        dismissDialog();
                                        ToastUtils.showShort("停止建图成功");
                                    }

                                    @Override
                                    public void onFailure(String msg) {
                                        dismissDialog();
                                        ToastUtils.showShort("停止建图失败%s:", msg);
                                    }
                                });
                    }
                });
                dialog.show(getSupportFragmentManager(), dialog.getTag());
            }
        });
        mBinding.includeLayoutCommonTitleBar.vpBackContainer.setOnClickListener(new View.OnClickListener() {
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
        mWebSocketMessageReceiver = new WebSocketMessageReceiver(this);
        IntentFilter filter = new IntentFilter(Constants.WEB_SOCKET_ACTION);
        registerReceiver(mWebSocketMessageReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mWebSocketMessageReceiver);
    }

    private static class WebSocketMessageReceiver extends BroadcastReceiver {
        private BuildMapActivity mBuildMapActivity;

        public WebSocketMessageReceiver(BuildMapActivity activity) {
            mBuildMapActivity = activity;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            Log.d(TAG, "收到：" + message);
            RobotoCreateMapIncrementDataDto websocketBean = GsonUtil.getInstance().fromJson(message, RobotoCreateMapIncrementDataDto.class);
            if (websocketBean == null || websocketBean.getBytes() == null) {
                return;
            }
            mBuildMapActivity.mBinding.bmv.setBuildNow(true);
            mBuildMapActivity.mBinding.bmv.setMapData(websocketBean);
        }
    }
}
