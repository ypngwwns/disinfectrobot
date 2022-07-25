package com.hitqz.disinfectionrobot.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.hitqz.disinfectionrobot.adapter.SelectDisinfectAreaAdapter;
import com.hitqz.disinfectionrobot.databinding.ActivityStartDisinfectBinding;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("CheckResult")
public class StartDisinfectActivity extends BaseActivity {

    ActivityStartDisinfectBinding mBinding;

    private SelectDisinfectAreaAdapter mSelectDisinfectAreaAdapter;
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
        mSelectDisinfectAreaAdapter = new SelectDisinfectAreaAdapter(mList);
        mBinding.lvDisinfectionArea.setAdapter(mSelectDisinfectAreaAdapter);
        mBinding.includeLayoutCommonTitleBar.ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
