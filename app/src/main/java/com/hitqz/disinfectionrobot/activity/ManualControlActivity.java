package com.hitqz.disinfectionrobot.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.hitqz.disinfectionrobot.data.SpeedRequest;
import com.hitqz.disinfectionrobot.databinding.ActivityManualControlBinding;
import com.hitqz.disinfectionrobot.net.BaseDataObserver;
import com.hitqz.disinfectionrobot.widget.RockerView;
import com.sonicers.commonlib.rx.RxSchedulers;

import java.lang.ref.WeakReference;

@SuppressLint("CheckResult")
public class ManualControlActivity extends BaseActivity {

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
        mBinding.includeLayoutCommonTitleBar.ibBack.setOnClickListener(new View.OnClickListener() {
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
                mHandler.sendEmptyMessage(10000);
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
                    case 10000:
                        activity.postSpeed();
                        sendEmptyMessageDelayed(10000, 100);
                }
            }
        }
    }
}
