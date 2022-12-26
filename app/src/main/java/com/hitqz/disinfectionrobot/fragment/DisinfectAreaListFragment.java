package com.hitqz.disinfectionrobot.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.hitqz.disinfectionrobot.activity.SetDisinfectAreaActivity;
import com.hitqz.disinfectionrobot.adapter.DisinfectAreaAdapter;
import com.hitqz.disinfectionrobot.data.MapArea;
import com.hitqz.disinfectionrobot.databinding.FragmentDisinfectAreaListBinding;
import com.hitqz.disinfectionrobot.dialog.DisinfectAreaNameDialog;
import com.hitqz.disinfectionrobot.event.RefreshEvent;
import com.hitqz.disinfectionrobot.net.BaseDataObserver;
import com.sonicers.commonlib.rx.RxSchedulers;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("CheckResult")
public class DisinfectAreaListFragment extends BaseFragment {
    public static final String TAG = DisinfectAreaListFragment.class.getSimpleName();

    FragmentDisinfectAreaListBinding mBinding;
    private DisinfectAreaAdapter mDisinfectAreaAdapter;
    private List<MapArea> mList = new ArrayList<>();

    private DisinfectAreaListFragment() {
        // Required empty public constructor
    }

    public static DisinfectAreaListFragment newInstance() {
        DisinfectAreaListFragment fragment = new DisinfectAreaListFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentDisinfectAreaListBinding.inflate(getLayoutInflater());
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDisinfectAreaAdapter = new DisinfectAreaAdapter(mList);
        mBinding.lvTimedTask.setAdapter(mDisinfectAreaAdapter);
        mDisinfectAreaAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                ((SetDisinfectAreaActivity) getActivity()).go2EditDisinfectArea(mList.get(position));
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
                DisinfectAreaNameDialog dialog = new DisinfectAreaNameDialog();
                dialog.setOnClickListener(new DisinfectAreaNameDialog.OnClickListener() {
                    @Override
                    public void onConfirm(String text) {
                        if (!isValid(text)) {
                            ToastUtils.showShort("已存在同名消毒区，请重新输入");
                            return;
                        }
                        dialog.dismiss();
                        MapArea mapArea = new MapArea();
                        mapArea.areaName = text;
                        ((SetDisinfectAreaActivity) getActivity()).go2EditDisinfectArea(mapArea);
                    }
                });
                dialog.show(getFragmentManager(), DisinfectAreaNameDialog.TAG);
            }
        });

        refreshAreaList();
    }

    private void refreshAreaList() {
        showDialog();
        getMSkyNet().areaListGet().compose(RxSchedulers.io_main())
                .subscribeWith(new BaseDataObserver<List<MapArea>>() {
                    @Override
                    public void onSuccess(List<MapArea> model) {
                        mList.clear();
                        mList.addAll(model);
                        mDisinfectAreaAdapter.notifyDataSetInvalidated();
                        dismissDialog();
                    }

                    @Override
                    public void onFailure(String msg) {
                        dismissDialog();
                        ToastUtils.showShort("获取到区域列表失败%s", msg);
                    }
                });
    }

    private boolean isValid(String name) {
        for (MapArea mapArea : mList) {
            if (name.equals(mapArea.areaName)) {
                return false;
            }
        }
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void oRefresh(RefreshEvent event) {
        refreshAreaList();
    }
}