package com.hitqz.disinfectionrobot.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.hitqz.disinfectionrobot.data.Cmd;
import com.hitqz.disinfectionrobot.data.SpeedRequest;
import com.hitqz.disinfectionrobot.databinding.ActivityManualControlBinding;
import com.hitqz.disinfectionrobot.net.BaseDataObserver;
import com.hitqz.disinfectionrobot.widget.RockerView;
import com.sonicers.commonlib.rx.RxSchedulers;

import java.lang.ref.WeakReference;

@SuppressLint("CheckResult")
public class ManualControlActivity extends BaseActivity {
    public static final int TIMING_SPEED_MESSAGE_ID = 10000;

    public final static float MAX_LINE_SPEED_VALUE = 0.25f;
    public final static float MAX_RADIUS_SPEED_VALUE = 0.4f;
    private final SpeedRequest mSpeedRequest = new SpeedRequest();
    ActivityManualControlBinding mBinding;
    private MyHandler mHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityManualControlBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mHandler = new MyHandler(this);
        mBinding.includeLayoutCommonTitleBar.vpBackContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBinding.rockerViewBottom.setOnTouchPointListener(new RockerView.OnTouchPointListener() {
            @Override
            public void position(float xPercent, float yPercent) {
                mSpeedRequest.linearSpeed = xPercent * MAX_LINE_SPEED_VALUE;

                if (Math.abs(yPercent) < 0.2) {
                    mSpeedRequest.angleSpeed = 0.0f;
                } else {
                    mSpeedRequest.angleSpeed = yPercent * MAX_RADIUS_SPEED_VALUE;
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

        mBinding.btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cmd cmd = new Cmd();
                cmd.cmd = 1;
                mISkyNet.disinfectCmd(cmd).compose(RxSchedulers.io_main())
                        .subscribeWith(new BaseDataObserver<Object>() {
                            @Override
                            public void onSuccess(Object model) {
                                ToastUtils.showShort("开启喷雾成功");
                            }

                            @Override
                            public void onFailure(String msg) {
                                ToastUtils.showShort("开启喷雾失败");
                            }
                        });
            }
        });

        mBinding.btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cmd cmd = new Cmd();
                cmd.cmd = 0;
                mISkyNet.disinfectCmd(cmd).compose(RxSchedulers.io_main())
                        .subscribeWith(new BaseDataObserver<Object>() {
                            @Override
                            public void onSuccess(Object model) {
                                ToastUtils.showShort("关闭喷雾成功");
                            }

                            @Override
                            public void onFailure(String msg) {
                                ToastUtils.showShort("关闭喷雾失败");
                            }
                        });
            }
        });
    }

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

    private static class MyHandler extends Handler {

        private WeakReference<ManualControlActivity> activityWeakReference;

        public MyHandler(ManualControlActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ManualControlActivity activity = activityWeakReference.get();
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
}
