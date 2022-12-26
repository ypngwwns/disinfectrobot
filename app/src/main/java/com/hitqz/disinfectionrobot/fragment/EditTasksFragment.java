package com.hitqz.disinfectionrobot.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.hitqz.disinfectionrobot.adapter.SelectDisinfectAreaAdapter;
import com.hitqz.disinfectionrobot.data.DisinfectTask;
import com.hitqz.disinfectionrobot.databinding.FragmentEditTasksBinding;
import com.hitqz.disinfectionrobot.net.BaseDataObserver;
import com.sonicers.commonlib.rx.RxSchedulers;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("CheckResult")
public class EditTasksFragment extends BaseFragment {

    public static final String TAG = EditTasksFragment.class.getSimpleName();
    FragmentEditTasksBinding mBinding;
    private SelectDisinfectAreaAdapter mSelectDisinfectAreaAdapter;
    private List<String> mList;
    private boolean mSelectedAllArea = true;

    private EditTasksFragment() {
        // Required empty public constructor
    }

    public static EditTasksFragment newInstance() {
        EditTasksFragment fragment = new EditTasksFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentEditTasksBinding.inflate(getLayoutInflater());
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mList = new ArrayList<>();
        mList.add("大厅");
        mList.add("一号会议室");
        mList.add("二号会议室");
        mSelectDisinfectAreaAdapter = new SelectDisinfectAreaAdapter(mList);
        mBinding.lvDisinfectionArea.setAdapter(mSelectDisinfectAreaAdapter);

        mBinding.includeLayoutCommonTitleBar.vpBackContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

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
        onSelectChanged();

        mBinding.fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
                DisinfectTask task = new DisinfectTask();
                task.jobTime = "01:00";
                task.taskType = 0;
                mSkyNet.addTask(task).compose(RxSchedulers.io_main())
                        .subscribeWith(new BaseDataObserver<Object>() {
                            @Override
                            public void onSuccess(Object model) {
                                ToastUtils.showShort("添加任务成功");
                                dismissDialog();
                            }

                            @Override
                            public void onFailure(String msg) {
                                ToastUtils.showShort("添加任务失败%s", msg);
                                dismissDialog();
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
}