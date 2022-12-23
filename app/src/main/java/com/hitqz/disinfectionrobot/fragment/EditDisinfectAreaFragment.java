package com.hitqz.disinfectionrobot.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.hitqz.disinfectionrobot.adapter.DisinfectPointAdapter;
import com.hitqz.disinfectionrobot.data.MapArea;
import com.hitqz.disinfectionrobot.data.MapAreaData;
import com.hitqz.disinfectionrobot.data.MapDataResponse;
import com.hitqz.disinfectionrobot.data.MapPose;
import com.hitqz.disinfectionrobot.data.NavigationPoint;
import com.hitqz.disinfectionrobot.databinding.FragmentEditDisinfectAreaBinding;
import com.hitqz.disinfectionrobot.dialog.CommonDialog;
import com.hitqz.disinfectionrobot.net.BaseDataObserver;
import com.sonicers.commonlib.rx.RxSchedulers;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@SuppressLint("CheckResult")
public class EditDisinfectAreaFragment extends BaseFragment {

    public static final String TAG = EditDisinfectAreaFragment.class.getSimpleName();
    FragmentEditDisinfectAreaBinding mBinding;
    private List<NavigationPoint> mNavigationPoints = new ArrayList<>();
    private DisinfectPointAdapter mDisinfectPointAdapter;
    private MapArea mMapArea;
    private MapDataResponse mMapDataResponse;

    private EditDisinfectAreaFragment() {
        // Required empty public constructor
    }

    public static EditDisinfectAreaFragment newInstance() {
        EditDisinfectAreaFragment fragment = new EditDisinfectAreaFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentEditDisinfectAreaBinding.inflate(getLayoutInflater());
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showDialog();
        Observable
                .fromCallable(() -> {
                    if (mChassisManager.mMapDataResponse == null) {
                        mChassisManager.loadChassisData();
                    }
                    return true;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean result) {
                        dismissDialog();
                        if (mChassisManager.mMapDataResponse != null) {
                            mMapDataResponse = mChassisManager.mMapDataResponse;
                            mBinding.navigationView.setBitmap(mMapDataResponse.bitmap);
                            mBinding.navigationView.setResolutionAndOrigin(mMapDataResponse.mapResolution, mMapDataResponse.mapOriginx,
                                    mMapDataResponse.mapOriginy);
                            getMapPose();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissDialog();
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        mBinding.navigationView.setSelectable(true);
        mBinding.navigationView.setNavigationPoints(mNavigationPoints);
        mBinding.includeLayoutCommonTitleBar.ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        mDisinfectPointAdapter = new DisinfectPointAdapter(getContext(), mBinding.navigationView.getSelectedNavigationPoints());
        mBinding.navigationView.setPointAdapter(mDisinfectPointAdapter);
        mBinding.dpll.setNavigationPointAdapter(mDisinfectPointAdapter);
        if (mMapArea != null) {
            mBinding.dpll.setName(mMapArea.areaName);
        }
        mBinding.dpll.setSaveListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<MapAreaData.Action> actions = new ArrayList<>();
                List<NavigationPoint> selectedPoints = mBinding.navigationView.getSelectedNavigationPoints();

                if (selectedPoints.size() == 0) {
                    ToastUtils.showShort("请至少选中一个消毒点");
                    return;
                }

                for (int i = 0; i < selectedPoints.size(); i++) {
                    MapAreaData.Action action = new MapAreaData.Action();
                    action.id = selectedPoints.get(i).id;
                    action.cmd = 1;
                    actions.add(action);
                }
                MapAreaData mapAreaData = new MapAreaData();
                mapAreaData.areaName = mMapArea.areaName;
                mapAreaData.mActions = actions;

                mSkyNet.areaPosAdd(mapAreaData).compose(RxSchedulers.io_main())
                        .subscribeWith(new BaseDataObserver<Object>() {
                            @Override
                            public void onSuccess(Object model) {
                                ToastUtils.showShort("保存消毒区域成功");
                                dismissDialog();
                            }

                            @Override
                            public void onFailure(String msg) {
                                ToastUtils.showShort("保存消毒区域失败");
                                dismissDialog();
                            }
                        });
            }
        });
        mBinding.dpll.setDeleteListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonDialog dialog = new CommonDialog();
                dialog.setOnClickListener(new CommonDialog.OnClickListener() {
                    @Override
                    public void onConfirm() {
                        showDialog();
                        mSkyNet.mapAreaDelete(mMapArea.id).compose(RxSchedulers.io_main())
                                .subscribeWith(new BaseDataObserver<Object>() {
                                    @Override
                                    public void onSuccess(Object model) {
                                        ToastUtils.showShort("删除消毒区域成功");
                                        dismissDialog();
                                    }

                                    @Override
                                    public void onFailure(String msg) {
                                        ToastUtils.showShort("删除消毒区域失败");
                                        dismissDialog();
                                    }
                                });
                    }
                });
                dialog.show(getFragmentManager(), CommonDialog.TAG);
            }
        });
    }

    public void setMapArea(MapArea mapArea) {
        mMapArea = mapArea;
    }

    private void getMapPose() {
        showDialog();
        mSkyNet.mapPosListGet().compose(RxSchedulers.io_main())
                .subscribeWith(new BaseDataObserver<List<MapPose>>() {
                    @Override
                    public void onSuccess(List<MapPose> model) {
                        for (MapPose mapPose : model) {
                            NavigationPoint navigationPoint = NavigationPoint.convertFromMapPose(mapPose);
                            mNavigationPoints.add(navigationPoint);
                        }
                        mBinding.navigationView.setNavigationPoints(mNavigationPoints);
                        mDisinfectPointAdapter.notifyDataSetInvalidated();
                        dismissDialog();
                    }

                    @Override
                    public void onFailure(String msg) {
                        ToastUtils.showShort("获取点位列表失败");
                        dismissDialog();
                    }
                });
    }
}