package com.hitqz.disinfectionrobot.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.hitqz.disinfectionrobot.adapter.SelectDisinfectAreaAdapter;
import com.hitqz.disinfectionrobot.data.DisinfectTask;
import com.hitqz.disinfectionrobot.data.MapArea;
import com.hitqz.disinfectionrobot.databinding.FragmentEditTasksBinding;
import com.hitqz.disinfectionrobot.dialog.CommonDialog;
import com.hitqz.disinfectionrobot.event.TaskRefreshEvent;
import com.hitqz.disinfectionrobot.net.BaseDataObserver;
import com.hitqz.disinfectionrobot.net.data.CleanTask;
import com.sonicers.commonlib.rx.RxSchedulers;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressLint("CheckResult")
public class EditTasksFragment extends BaseFragment {

    public static final String TAG = EditTasksFragment.class.getSimpleName();
    private final SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("HH:mm");
    FragmentEditTasksBinding mBinding;
    private SelectDisinfectAreaAdapter mSelectDisinfectAreaAdapter;
    private List<MapArea> mList = new ArrayList<>();
    private boolean mSelectedAllArea = true;
    private CleanTask mItem;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentEditTasksBinding.inflate(getLayoutInflater());
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        mBinding.includeLayoutCommonTitleBar.tvSsid.setText("");
        mSelectDisinfectAreaAdapter = new SelectDisinfectAreaAdapter(mList);
        mBinding.lvDisinfectionArea.setAdapter(mSelectDisinfectAreaAdapter);
//        mBinding.tpTime.setIs24HourView(true);
//        if (TextUtils.isEmpty(mItem.taskName)) {
//            mBinding.fabDelete.setVisibility(View.GONE);
//        } else {
//            mSelectedAllArea = mItem.jobStatus == 0;
//            try {
//                Date date = mSimpleDateFormat.parse(mItem.startTime);
//                mBinding.tpTime.setHour(date.getHours());
//                mBinding.tpTime.setMinute(date.getMinutes());
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }

        mBinding.includeLayoutCommonTitleBar.vpBackContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        mBinding.vp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectedAllArea) {
                    return;
                }
                mSelectedAllArea = true;
                onSelectChanged();
            }
        });
        mBinding.vp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mSelectedAllArea) {
                    return;
                }
                mSelectedAllArea = false;
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
                        mSkyNet.deleteTask(mItem.id).compose(RxSchedulers.io_main())
                                .subscribeWith(new BaseDataObserver<Object>() {
                                    @Override
                                    public void onSuccess(Object model) {
                                        ToastUtils.showShort("删除任务成功");
                                        EventBus.getDefault().post(new TaskRefreshEvent());
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

        mBinding.fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
                DisinfectTask task = new DisinfectTask();
//                task.jobTime = mBinding.tpTime.getHour() + ":" + mBinding.tpTime.getMinute();
                if (mSelectedAllArea) {
                    task.taskType = 0;
                } else {
                    task.taskType = 1;
                    int pos = mSelectDisinfectAreaAdapter.getSelectedPos();
                    task.workArea = mList.get(pos).id;
                }

                if (!TextUtils.isEmpty(mItem.taskName)) {
                    task.id = mItem.id;
                    mSkyNet.updateTask(task).compose(RxSchedulers.io_main())
                            .subscribeWith(new BaseDataObserver<Object>() {
                                @Override
                                public void onSuccess(Object model) {
                                    ToastUtils.showShort("更新任务成功");
                                    EventBus.getDefault().post(new TaskRefreshEvent());
                                    dismissDialog();
                                }

                                @Override
                                public void onFailure(String msg) {
                                    ToastUtils.showShort("更新任务失败%s", msg);
                                    dismissDialog();
                                }
                            });
                } else {
                    mSkyNet.addTask(task).compose(RxSchedulers.io_main())
                            .subscribeWith(new BaseDataObserver<Object>() {
                                @Override
                                public void onSuccess(Object model) {
                                    ToastUtils.showShort("新增任务成功");
                                    EventBus.getDefault().post(new TaskRefreshEvent());
                                    dismissDialog();
                                }

                                @Override
                                public void onFailure(String msg) {
                                    ToastUtils.showShort("新增任务失败%s", msg);
                                    dismissDialog();
                                }
                            });
                }
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
                        int index = findIndex(model);
                        mSelectDisinfectAreaAdapter.setSelectedPos(index);
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

    private int findIndex(List<MapArea> mapAreas) {
//        if (TextUtils.isEmpty(mItem.areaName)) {
//            return -1;
//        }
//        if (mItem.taskType == 0) {
//            return -1;
//        }
//
//        for (int i = 0; i < mapAreas.size(); i++) {
//            if (mapAreas.get(i).id.equals(mItem.workArea.id)) {
//                return i;
//            }
//        }
        return -1;
    }

    public void setTask(CleanTask task) {
        mItem = task;
    }
}