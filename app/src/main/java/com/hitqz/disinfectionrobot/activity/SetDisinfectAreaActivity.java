package com.hitqz.disinfectionrobot.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.hitqz.disinfectionrobot.databinding.ActivitySetDisinfectAreaBinding;

@SuppressLint("CheckResult")
public class SetDisinfectAreaActivity extends BaseActivity {

    ActivitySetDisinfectAreaBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivitySetDisinfectAreaBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mBinding.includeLayoutCommonTitleBar.ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
