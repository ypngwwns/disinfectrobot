package com.hitqz.disinfectionrobot.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.hitqz.disinfectionrobot.data.SpeedRequest;
import com.hitqz.disinfectionrobot.databinding.ActivityManualControlBinding;
import com.hitqz.disinfectionrobot.net.BaseDataObserver;
import com.hitqz.disinfectionrobot.widget.RockerView;
import com.sonicers.commonlib.rx.RxSchedulers;

@SuppressLint("CheckResult")
public class ManualControlActivity extends BaseActivity {

    public final static float MAX_LINE_SPEED_VALUE = 0.25f;
    public final static float MAX_RADIUS_SPEED_VALUE = 0.4f;
    private final SpeedRequest mSpeedRequest = new SpeedRequest();
    ActivityManualControlBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityManualControlBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
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
            public void onFinish() {

            }
        });
    }
}
