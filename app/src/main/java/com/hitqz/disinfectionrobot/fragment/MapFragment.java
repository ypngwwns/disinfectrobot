package com.hitqz.disinfectionrobot.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.hitqz.disinfectionrobot.data.Goal;
import com.hitqz.disinfectionrobot.data.MapData;
import com.hitqz.disinfectionrobot.data.NavigationPoint;
import com.hitqz.disinfectionrobot.databinding.FragmentMapBinding;
import com.hitqz.disinfectionrobot.net.BaseDataObserver;
import com.hitqz.disinfectionrobot.util.PathUtil;
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

    FragmentMapBinding mBinding;
    private MapData mMapData;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentMapBinding.inflate(getLayoutInflater());
        return mBinding.getRoot();
    }

    private void initMap(String mapCode) {
        mMapData = new MapData(PathUtil.getMapPGMFile(getMContext().getApplicationContext(), mapCode),
                PathUtil.getMapYmlFile(getMContext().getApplicationContext(), mapCode));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showDialog();

        Observable
                .fromCallable(() -> {
                    initMap("map0622");
                    return 0;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        dismissDialog();
                        mBinding.navigationView.setBitmap(mMapData.bitmap);
                        mBinding.navigationView.setResolutionAndOrigin(mMapData.resolution, mMapData.originX, mMapData.originY);
                        initGoalList();
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

    private void initGoalList() {
        showDialog();
        getMSkyNet().getByMapCheck("map0622")
                .compose(RxSchedulers.io_main())
                .subscribeWith(new BaseDataObserver<List<Goal>>() {
                    @Override
                    public void onSuccess(List<Goal> model) {
                        if (model != null && model.size() > 0) {
                            List<NavigationPoint> navigationPoints = new ArrayList<>();
                            for (Goal goal : model) {
                                NavigationPoint navigationPoint = new NavigationPoint();
                                navigationPoint.rawX = goal.getPosx();
                                navigationPoint.rawY = goal.getPosy();
                                navigationPoint.radian = goal.getYaw();
                                navigationPoints.add(navigationPoint);
                            }
                            mBinding.navigationView.setNavigationPoints(navigationPoints);
                            ToastUtils.showShort("获取点位列表成功");
                            dismissDialog();
                        }
                    }

                    @Override
                    public void onFailure(String msg) {
                        dismissDialog();
                        ToastUtils.showShort("获取点位列表失败");
                    }
                });
    }
}