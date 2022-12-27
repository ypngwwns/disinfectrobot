package com.hitqz.disinfectionrobot.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.hitqz.disinfectionrobot.adapter.SelectDisinfectAreaAdapter;
import com.hitqz.disinfectionrobot.data.MapArea;
import com.hitqz.disinfectionrobot.data.TempTask;
import com.hitqz.disinfectionrobot.databinding.ActivityStartDisinfectBinding;
import com.hitqz.disinfectionrobot.net.BaseDataObserver;
import com.sonicers.commonlib.rx.RxSchedulers;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("CheckResult")
public class StartDisinfectActivity extends BaseActivity {

    ActivityStartDisinfectBinding mBinding;

    private SelectDisinfectAreaAdapter mSelectDisinfectAreaAdapter;
    private List<MapArea> mList = new ArrayList<>();

    private boolean mSelectedAllArea = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityStartDisinfectBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mSelectDisinfectAreaAdapter = new SelectDisinfectAreaAdapter(mList);
        mBinding.lvDisinfectionArea.setAdapter(mSelectDisinfectAreaAdapter);
        mBinding.includeLayoutCommonTitleBar.vpBackContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        onSelectChanged();
        mBinding.rbAllArea.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mSelectedAllArea == isChecked) {
                    return;
                }
                mSelectedAllArea = isChecked;
                onSelectChanged();
            }
        });
        mBinding.rbPartArea.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mSelectedAllArea == !isChecked) {
                    return;
                }
                mSelectedAllArea = !isChecked;
                onSelectChanged();
            }
        });
        refreshAreaList();
        mBinding.btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
                TempTask task = new TempTask();
                task.taskType = mSelectedAllArea ? 0 : 1;
                if (!mSelectedAllArea) {
                    int pos = mSelectDisinfectAreaAdapter.getSelectedPos();
                    task.workArea = mList.get(pos).id;
                }
                mISkyNet.addJobNow(task).compose(RxSchedulers.io_main())
                        .subscribeWith(new BaseDataObserver<Object>() {
                            @Override
                            public void onSuccess(Object model) {
                                ToastUtils.showShort("添加任务，立即执行成功");
                                dismissDialog();
                            }

                            @Override
                            public void onFailure(String msg) {
                                dismissDialog();
                                ToastUtils.showShort("添加任务，立即执行失败%s", msg);
                            }
                        });
            }
        });
    }

    private void onSelectChanged() {

        if (mSelectedAllArea) {
            mBinding.rbAllArea.setChecked(true);
            mBinding.rbPartArea.setChecked(false);
            mBinding.tvSelectDisinfectionArea.setVisibility(View.GONE);
            mBinding.lvDisinfectionArea.setVisibility(View.GONE);
        } else {
            mBinding.rbAllArea.setChecked(false);
            mBinding.rbPartArea.setChecked(true);
            mBinding.tvSelectDisinfectionArea.setVisibility(View.VISIBLE);
            mBinding.lvDisinfectionArea.setVisibility(View.VISIBLE);
            mSelectDisinfectAreaAdapter.notifyDataSetChanged();
        }
    }

    private void refreshAreaList() {
        showDialog();
        mISkyNet.areaListGet().compose(RxSchedulers.io_main())
                .subscribeWith(new BaseDataObserver<List<MapArea>>() {
                    @Override
                    public void onSuccess(List<MapArea> model) {
                        mList.clear();
                        mList.addAll(model);
                        mSelectDisinfectAreaAdapter.notifyDataSetInvalidated();
                        dismissDialog();
                    }

                    @Override
                    public void onFailure(String msg) {
                        dismissDialog();
                        ToastUtils.showShort("获取到区域列表失败%s", msg);
                    }
                });
    }
}
