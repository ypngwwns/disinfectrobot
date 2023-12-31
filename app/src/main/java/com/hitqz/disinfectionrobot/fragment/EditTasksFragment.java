package com.hitqz.disinfectionrobot.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.hitqz.disinfectionrobot.adapter.SelectDisinfectAreaAdapter;
import com.hitqz.disinfectionrobot.constant.Constants;
import com.hitqz.disinfectionrobot.data.MapArea;
import com.hitqz.disinfectionrobot.databinding.FragmentEditTasksBinding;
import com.hitqz.disinfectionrobot.dialog.CommonDialog;
import com.hitqz.disinfectionrobot.dialog.TimePickDialog;
import com.hitqz.disinfectionrobot.event.TaskRefreshEvent;
import com.hitqz.disinfectionrobot.net.BaseDataObserver;
import com.hitqz.disinfectionrobot.net.data.CleanTask;
import com.hitqz.disinfectionrobot.widget.editspinner.SimpleAdapter;
import com.sonicers.commonlib.rx.RxSchedulers;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressLint("CheckResult")
public class EditTasksFragment extends BaseFragment {

    public static final String TAG = EditTasksFragment.class.getSimpleName();
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
        SimpleAdapter simpAdapter = new SimpleAdapter(mContext, Arrays.asList(Constants.taskTypes));
        mBinding.es4.setAdapter(simpAdapter);
        if (mItem == null) {
            mBinding.fabDelete.setVisibility(View.GONE);
            mBinding.et3.setText("1");
            mBinding.es4.setText("一次");
        } else {
            mBinding.et1.setText(mItem.taskName);
            mBinding.tvStartTime.setText(mItem.startTime);
            mBinding.tvEndTime.setText(mItem.endTime);
            mBinding.et3.setText(String.valueOf(mItem.circle));
            mBinding.es4.setText(mItem.taskType == 0 ? "一次" : "每天");
            mBinding.fabDelete.setVisibility(View.VISIBLE);
        }

        mBinding.fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonDialog dialog = new CommonDialog();
                dialog.setOnClickListener(new CommonDialog.OnClickListener() {
                    @Override
                    public void onConfirm() {
                        showDialog();
                        mISkyNet.deleteCleanTask(mItem.id).compose(RxSchedulers.io_main())
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
                if (checkInvalid()) {
                    return;
                }
                if (mItem == null) {
                    mItem = new CleanTask();
                    assignBody();
                    showDialog();
                    mISkyNet.addCleanTask(mItem)
                            .compose(RxSchedulers.io_main())
                            .subscribeWith(new BaseDataObserver<Object>() {
                                @Override
                                public void onSuccess(Object model) {
                                    dismissDialog();
                                    refreshList();
                                    ToastUtils.showShort("添加成功");
                                }

                                @Override
                                public void onFailure(String msg) {
                                    mItem = null;
                                    ToastUtils.showShort("添加失败");
                                }
                            });
                } else {
                    assignBody();
                    showDialog();
                    mISkyNet.updateCleanTask(mItem)
                            .compose(RxSchedulers.io_main())
                            .subscribeWith(new BaseDataObserver<Object>() {
                                @Override
                                public void onSuccess(Object model) {
                                    dismissDialog();
                                    refreshList();
                                    ToastUtils.showShort("更新成功");
                                }

                                @Override
                                public void onFailure(String msg) {
                                    mItem = null;
                                    ToastUtils.showShort("更新失败");
                                }
                            });
                }
            }
        });
        mBinding.tvStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickDialog dialog = new TimePickDialog();
                if (mItem != null) {
                    dialog.setTime(mItem.startTime);
                }
                dialog.setOnClickListener(new TimePickDialog.OnClickListener() {
                    @Override
                    public void onConfirm() {
                        mBinding.tvStartTime.setText(dialog.getTime());
                    }
                });
                dialog.show(getFragmentManager(), TimePickDialog.TAG);
            }
        });
        mBinding.tvEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickDialog dialog = new TimePickDialog();
                if (mItem != null) {
                    dialog.setTime(mItem.endTime);
                }
                dialog.setOnClickListener(new TimePickDialog.OnClickListener() {
                    @Override
                    public void onConfirm() {
                        mBinding.tvEndTime.setText(dialog.getTime());
                    }
                });
                dialog.show(getFragmentManager(), TimePickDialog.TAG);
            }
        });
    }

    private void refreshList() {
        EventBus.getDefault().post(new TaskRefreshEvent());
    }

    private boolean checkInvalid() {
        boolean invalid = false;
        if (TextUtils.isEmpty(mBinding.et1.getText().toString())) {
            mBinding.et1.setHintTextColor(Color.parseColor("#ff0000"));
            invalid = true;
        }

        if (TextUtils.isEmpty(mBinding.et3.getText().toString())) {
            mBinding.et3.setHintTextColor(Color.parseColor("#ff0000"));
            invalid = true;
        } else {
            int circle = Integer.parseInt(mBinding.et3.getText().toString());
            if (circle < 1 || circle > 100) {
                ToastUtils.showShort("循环次数必须在1-100之间");
                invalid = true;
            }
        }

        return invalid;
    }

    private void assignBody() {
        mItem.taskName = (mBinding.et1.getText().toString());
        mItem.startTime = mBinding.tvStartTime.getText().toString();
        mItem.endTime = mBinding.tvEndTime.getText().toString();
        if (!TextUtils.isEmpty(mBinding.et3.getText().toString())) {
            mItem.circle = Integer.parseInt(mBinding.et3.getText().toString());
        }
        if ("一次".equals(mBinding.es4.getText())) {
            mItem.taskType = 0;
        } else {
            mItem.taskType = 1;
        }
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
        getMISkyNet().areaListGet().compose(RxSchedulers.io_main())
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

    public void setItem(CleanTask item) {
        mItem = item;
    }
}