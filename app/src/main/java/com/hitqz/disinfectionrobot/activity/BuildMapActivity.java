package com.hitqz.disinfectionrobot.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.hitqz.disinfectionrobot.DisinfectRobotApplication;
import com.hitqz.disinfectionrobot.data.MapCode;
import com.hitqz.disinfectionrobot.data.NavigationPoint;
import com.hitqz.disinfectionrobot.data.RobotoCreateMapIncrementDataDto;
import com.hitqz.disinfectionrobot.data.SpeedRequest;
import com.hitqz.disinfectionrobot.databinding.ActivityBuildMapBinding;
import com.hitqz.disinfectionrobot.dialog.CommonDialog;
import com.hitqz.disinfectionrobot.dialog.SaveMapDialog;
import com.hitqz.disinfectionrobot.net.BaseDataObserver;
import com.hitqz.disinfectionrobot.net.ws.JWebSocketClientService;
import com.hitqz.disinfectionrobot.widget.RockerView;
import com.sonicers.commonlib.rx.RxSchedulers;
import com.sonicers.commonlib.singleton.GsonUtil;

import java.lang.ref.WeakReference;

@SuppressLint("CheckResult")
public class BuildMapActivity extends BaseActivity {
    public static final String TAG = BuildMapActivity.class.getSimpleName();
    public static final int TIMING_SPEED_MESSAGE_ID = 10000;
    public final static float MAX_LINE_SPEED_VALUE = 0.25f;
    public final static float MAX_RADIUS_SPEED_VALUE = 0.4f;
    private final SpeedRequest mSpeedRequest = new SpeedRequest();
    ActivityBuildMapBinding mBinding;
    private WebSocketMessageReceiver mWebSocketMessageReceiver;
    private MyHandler mHandler;
    private NavigationPoint mRobotPos = new NavigationPoint();

    private void postSpeed() {
        Log.d("postSpeed", "mSpeedRequest.linearSpeed:" + mSpeedRequest.linearSpeed + "  mSpeedRequest.angleSpeed:" + mSpeedRequest.angleSpeed);
        mISkyNet.ctrlMove(mSpeedRequest).compose(RxSchedulers.io_main())
                .subscribeWith(new BaseDataObserver<Object>() {
                    @Override
                    public void onSuccess(Object model) {

                    }

                    @Override
                    public void onFailure(String msg) {
                    }
                });
    }

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
                                            mBinding.bmv.setBuildNow(false);
                                            mBinding.bmv.reset();
                                            mBinding.bmv.postInvalidate();
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
                    DisinfectRobotApplication.instance.jWebSClientService.sendMsg("{\"topic\": \"CREATE_2D_MAP_DATA\"}");
                    mISkyNet.buildMap().compose(RxSchedulers.io_main())
                            .subscribeWith(new BaseDataObserver<Object>() {
                                @Override
                                public void onSuccess(Object model) {
                                    mBinding.bmv.setBuildNow(true);
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
                                        mBinding.bmv.setBuildNow(false);
                                        mBinding.bmv.reset();
                                        mBinding.bmv.postInvalidate();
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
        mHandler = new MyHandler(this);
        mBinding.rockerViewBottom.setOnTouchPointListener(new RockerView.OnTouchPointListener() {
            @Override
            public void position(float xPercent, float yPercent) {
                mSpeedRequest.linearSpeed = xPercent * MAX_LINE_SPEED_VALUE;

                if (Math.abs(yPercent) < 0.2) {
                    mSpeedRequest.angleSpeed = 0.0f;
                } else {
                    mSpeedRequest.angleSpeed = -yPercent * MAX_RADIUS_SPEED_VALUE;
                }
                mHandler.sendEmptyMessage(TIMING_SPEED_MESSAGE_ID);
            }

            @Override
            public void onFinish() {
                mHandler.removeCallbacksAndMessages(null);
                mSpeedRequest.linearSpeed = 0d;
                mSpeedRequest.angleSpeed = 0d;
                postSpeed();
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
        DisinfectRobotApplication.instance.addWebSocketCallback(mWebSocketMessageReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DisinfectRobotApplication.instance.removeWebSocketCallback(mWebSocketMessageReceiver);
    }

    private static class MyHandler extends Handler {

        private WeakReference<BuildMapActivity> activityWeakReference;

        public MyHandler(BuildMapActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            BuildMapActivity activity = activityWeakReference.get();
            if (activity != null) {
                //处理handler消息
                switch (msg.what) {
                    case ManualControlActivity.TIMING_SPEED_MESSAGE_ID:
                        removeMessages(TIMING_SPEED_MESSAGE_ID);
                        activity.postSpeed();
                        sendEmptyMessageDelayed(TIMING_SPEED_MESSAGE_ID, 100);
                }
            }
        }
    }

    private static class WebSocketMessageReceiver implements JWebSocketClientService.WebSocketCallback {
        private BuildMapActivity mActivity;

        public WebSocketMessageReceiver(BuildMapActivity activity) {
            mActivity = activity;
        }

        @Override
        public void onMessage(String message) {

            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    RobotoCreateMapIncrementDataDto robotoCreateMapIncrementDataDto = GsonUtil.getInstance().fromJson(message, RobotoCreateMapIncrementDataDto.class);
                    if (robotoCreateMapIncrementDataDto == null || robotoCreateMapIncrementDataDto.getBytes() == null) {
                        return;
                    }

                    mActivity.mBinding.bmv.setMapData(robotoCreateMapIncrementDataDto);
                    mActivity.mRobotPos.rawX = robotoCreateMapIncrementDataDto.getRobotInfoDto().getX();
                    mActivity.mRobotPos.rawY = robotoCreateMapIncrementDataDto.getRobotInfoDto().getY();
                    mActivity.mRobotPos.radian = robotoCreateMapIncrementDataDto.getRobotInfoDto().getYaw();
                    mActivity.mBinding.bmv.setRobotPos(mActivity.mRobotPos);
                    mActivity.mBinding.bmv.postInvalidate();
                }
            });
        }

        @Override
        public void onConnectSuccess(String s) {

        }
    }
}
