package com.hitqz.disinfectionrobot.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.hitqz.disinfectionrobot.adapter.DisinfectAreaAdapter;
import com.hitqz.disinfectionrobot.databinding.ActivityStartDisinfectBinding;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("CheckResult")
public class StartDisinfectActivity extends BaseActivity {

    ActivityStartDisinfectBinding mBinding;

    private DisinfectAreaAdapter mDisinfectAreaAdapter;
    private List<String> mList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityStartDisinfectBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mList = new ArrayList<>();
        mList.add("大厅");
        mList.add("一号会议室");
        mList.add("二号会议室");
        mDisinfectAreaAdapter = new DisinfectAreaAdapter(mList);
        mBinding.lvDisinfectionArea.setAdapter(mDisinfectAreaAdapter);
        mBinding.includeLayoutCommonTitleBar.ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
