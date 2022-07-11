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
import com.hitqz.disinfectionrobot.databinding.FragmentMapBinding;
import com.hitqz.disinfectionrobot.net.BaseDataObserver;
import com.hitqz.disinfectionrobot.util.PathUtil;
import com.sonicers.commonlib.rx.RxSchedulers;

import java.io.IOException;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
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

    private void initMap(String mapCode) throws IOException {
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
                .subscribe((result) -> {
                    mBinding.navigationView.setBitmap(mMapData.bitmap);
                    initGoalList();
                    dismissDialog();
                });


    }

    private void initGoalList() {
        showDialog();
        getMSkyNet().getByMapCheck("map0622")
                .compose(RxSchedulers.io_main())
                .subscribeWith(new BaseDataObserver<List<Goal>>() {
                    @Override
                    public void onSuccess(List<Goal> model) {
                        ToastUtils.showShort("获取点位列表成功");
                        dismissDialog();
                    }

                    @Override
                    public void onFailure(String msg) {
                        dismissDialog();
                        ToastUtils.showShort("获取点位列表失败");
                    }
                });
    }
}