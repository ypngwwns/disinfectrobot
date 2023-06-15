package com.hitqz.disinfectionrobot.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.hitqz.disinfectionrobot.activity.DisinfectRegularlyActivity;
import com.hitqz.disinfectionrobot.adapter.TimedTaskAdapter;
import com.hitqz.disinfectionrobot.data.Task;
import com.hitqz.disinfectionrobot.databinding.FragmentDisinfectRegularlyBinding;
import com.hitqz.disinfectionrobot.event.TaskRefreshEvent;
import com.hitqz.disinfectionrobot.net.BaseDataObserver;
import com.hitqz.disinfectionrobot.net.data.CleanTask;
import com.sonicers.commonlib.rx.RxSchedulers;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("CheckResult")
public class DisinfectRegularlyFragment extends BaseFragment {
    public static final String TAG = DisinfectRegularlyFragment.class.getSimpleName();

    FragmentDisinfectRegularlyBinding mBinding;
    private TimedTaskAdapter mTimedTaskAdapter;
    private List<CleanTask> mList = new ArrayList<>();

    private DisinfectRegularlyFragment() {
        // Required empty public constructor
    }

    public static DisinfectRegularlyFragment newInstance() {
        DisinfectRegularlyFragment fragment = new DisinfectRegularlyFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentDisinfectRegularlyBinding.inflate(getLayoutInflater());
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTimedTaskAdapter = new TimedTaskAdapter(getContext(), mList);
        mTimedTaskAdapter.setOnCheckChangeListener(new TimedTaskAdapter.IOnCheckChangeListener() {
            @Override
            public void onCheckChange(int position, boolean check) {
                if (check == (mList.get(position).jobStatus == 0)) {
                    return;
                }
                Task task = new Task();
                task.id = mList.get(position).id;
                task.jobStatus = check ? 0 : 1;
                getMSkyNet().activeJob(task).compose(RxSchedulers.io_main())
                        .subscribeWith(new BaseDataObserver<Object>() {
                            @Override
                            public void onSuccess(Object model) {
                                ToastUtils.showShort("修改任务状态成功");
                                refreshList();
                                dismissDialog();
                            }

                            @Override
                            public void onFailure(String msg) {
                                dismissDialog();
                                ToastUtils.showShort("修改任务状态失败%s", msg);
                            }
                        });
            }
        });
        mBinding.lvTimedTask.setAdapter(mTimedTaskAdapter);
        mTimedTaskAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                ((DisinfectRegularlyActivity) getActivity()).go2EditTask(mList.get(position));
            }
        });
        mBinding.includeLayoutCommonTitleBar.vpBackContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        mBinding.fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CleanTask item = new CleanTask();
                ((DisinfectRegularlyActivity) getActivity()).go2EditTask(item);
            }
        });
        refreshList();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void oRefresh(TaskRefreshEvent event) {
        refreshList();
    }

    private void refreshList() {
        showDialog();
        getMSkyNet().cleanTaskListGet().compose(RxSchedulers.io_main())
                .subscribeWith(new BaseDataObserver<List<CleanTask>>() {
                    @Override
                    public void onSuccess(List<CleanTask> model) {
                        mList.clear();
                        mList.addAll(model);
                        mTimedTaskAdapter.notifyDataSetInvalidated();
                        dismissDialog();
                    }

                    @Override
                    public void onFailure(String msg) {
                        dismissDialog();
                        ToastUtils.showShort("获取到任务列表失败%s", msg);
                    }
                });
    }
}