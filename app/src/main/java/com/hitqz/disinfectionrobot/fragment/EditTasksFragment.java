package com.hitqz.disinfectionrobot.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.hitqz.disinfectionrobot.adapter.SelectDisinfectAreaAdapter;
import com.hitqz.disinfectionrobot.data.DisinfectTask;
import com.hitqz.disinfectionrobot.data.MapArea;
import com.hitqz.disinfectionrobot.data.Task;
import com.hitqz.disinfectionrobot.databinding.FragmentEditTasksBinding;
import com.hitqz.disinfectionrobot.dialog.CommonDialog;
import com.hitqz.disinfectionrobot.net.BaseDataObserver;
import com.sonicers.commonlib.rx.RxSchedulers;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("CheckResult")
public class EditTasksFragment extends BaseFragment {

    public static final String TAG = EditTasksFragment.class.getSimpleName();
    FragmentEditTasksBinding mBinding;
    private SelectDisinfectAreaAdapter mSelectDisinfectAreaAdapter;
    private List<MapArea> mList = new ArrayList<>();
    private boolean mSelectedAllArea = true;
    private Task mTask;
    private int mHour;
    private int mMinute;

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
        refreshAreaList();

        mBinding.fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonDialog dialog = new CommonDialog();
                dialog.setOnClickListener(new CommonDialog.OnClickListener() {
                    @Override
                    public void onConfirm() {
                        showDialog();
                        mSkyNet.deleteTask(mTask.id).compose(RxSchedulers.io_main())
                                .subscribeWith(new BaseDataObserver<Object>() {
                                    @Override
                                    public void onSuccess(Object model) {
                                        ToastUtils.showShort("删除任务成功");
                                        dismissDialog();
                                    }

                                    @Override
                                    public void onFailure(String msg) {
                                        ToastUtils.showShort("删除任务失败%s", msg);
                                        dismissDialog();
                                    }
                                });
                    }
                });
                dialog.show(getFragmentManager(), CommonDialog.TAG);
            }
        });
        mBinding.tpTime.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                mHour = hourOfDay;
                mMinute = minute;
            }
        });

        mBinding.fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
                DisinfectTask task = new DisinfectTask();
//                SimpleDateFormat format = new SimpleDateFormat("HH:mm");
//                Date date = new Date();
//                date.setHours(mBinding.tpTime.);
//                String time = format.format()
                task.jobTime = "01:00";
                if (mSelectedAllArea) {
                    task.taskType = 0;
                } else {
                    task.taskType = 1;
                    int pos = mSelectDisinfectAreaAdapter.getSelectedPos();
                    task.workArea = mList.get(pos).id;
                }

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

    private void refreshAreaList() {
        showDialog();
        getMSkyNet().areaListGet().compose(RxSchedulers.io_main())
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

    public void setTask(Task task) {
        mTask = task;
    }
}