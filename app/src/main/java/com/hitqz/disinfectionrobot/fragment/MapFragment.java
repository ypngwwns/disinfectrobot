package com.hitqz.disinfectionrobot.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.hitqz.disinfectionrobot.data.MapDataResponse;
import com.hitqz.disinfectionrobot.data.MapPose;
import com.hitqz.disinfectionrobot.data.NavigationPoint;
import com.hitqz.disinfectionrobot.data.RobotStatus;
import com.hitqz.disinfectionrobot.databinding.FragmentMapBinding;
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
public class MapFragment extends BaseFragment {

    private final List<NavigationPoint> mNavigationPoints = new ArrayList<>();
    FragmentMapBinding mBinding;
    private MapDataResponse mMapDataResponse;
    private NavigationPoint mRobotPos = new NavigationPoint();
    private NavigationPoint mRechargePos;

    private MapFragment() {
        // Required empty public constructor
    }

    public static MapFragment newInstance() {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentMapBinding.inflate(getLayoutInflater());
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
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
                            mBinding.tvMapCode.setText(mMapDataResponse.mapName);
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
    }

    private void getMapPose() {
        showDialog();
        mSkyNet.mapPosListGet().compose(RxSchedulers.io_main())
                .subscribeWith(new BaseDataObserver<List<MapPose>>() {
                    @Override
                    public void onSuccess(List<MapPose> model) {
                        for (MapPose mapPose : model) {
                            NavigationPoint navigationPoint = new NavigationPoint();
                            navigationPoint.mapCode = mapPose.mapCode;
                            navigationPoint.name = mapPose.name;
                            navigationPoint.rawX = mapPose.posx;
                            navigationPoint.rawY = mapPose.posy;
                            navigationPoint.radian = mapPose.yaw;
                            navigationPoint.id = mapPose.id;
                            if ("2".equals(mapPose.type)) {
                                mRechargePos = navigationPoint;
                            } else {
                                mNavigationPoints.add(navigationPoint);
                            }
                        }
                        mBinding.navigationView.setRechargePos(mRechargePos);
                        mBinding.navigationView.setNavigationPoints(mNavigationPoints);
                        mBinding.navigationView.setNavigationPoints(mNavigationPoints);
                        dismissDialog();
                    }

                    @Override
                    public void onFailure(String msg) {
                        ToastUtils.showShort("获取点位列表失败%s:", msg);
                        dismissDialog();
                    }
                });
    }

    public void refresh(RobotStatus robotStatus) {
        mRobotPos.rawX = robotStatus.getCurrentPos().getX();
        mRobotPos.rawY = robotStatus.getCurrentPos().getY();
        mRobotPos.radian = robotStatus.getCurrentPos().getYaw();
        mBinding.navigationView.setRobotPoint(mRobotPos);
        mBinding.navigationView.setLaserScan(robotStatus.getLaserOriginData());
        mBinding.navigationView.postInvalidate();
    }
}