package com.hitqz.disinfectionrobot.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.hitqz.disinfectionrobot.databinding.ActivityBuildMapBinding;
import com.hitqz.disinfectionrobot.dialog.CommonDialog;

@SuppressLint("CheckResult")
public class BuildMapActivity extends BaseActivity {

    ActivityBuildMapBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityBuildMapBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
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
}
