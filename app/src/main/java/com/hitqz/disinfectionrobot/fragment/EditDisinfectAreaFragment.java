package com.hitqz.disinfectionrobot.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hitqz.disinfectionrobot.data.MapData;
import com.hitqz.disinfectionrobot.data.NavigationPoint;
import com.hitqz.disinfectionrobot.databinding.FragmentEditDisinfectAreaBinding;
import com.hitqz.disinfectionrobot.util.DBHelper;
import com.hitqz.disinfectionrobot.util.PathUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class EditDisinfectAreaFragment extends BaseFragment {

    public static final String TAG = EditDisinfectAreaFragment.class.getSimpleName();
    FragmentEditDisinfectAreaBinding mBinding;
    private MapData mMapData;
    private List<NavigationPoint> mNavigationPoints = new ArrayList<>();

    private EditDisinfectAreaFragment() {
        // Required empty public constructor
    }

    public static EditDisinfectAreaFragment newInstance() {
        EditDisinfectAreaFragment fragment = new EditDisinfectAreaFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private void initMap(String mapCode) {
        mMapData = new MapData(PathUtil.getMapPGMFile(getContext(), mapCode),
                PathUtil.getMapYmlFile(getContext(), mapCode));
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
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissDialog();
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        List<NavigationPoint> list = DBHelper.findAllNavigationPoint("map0622");
        mNavigationPoints.clear();
        mNavigationPoints.addAll(list);
        mBinding.navigationView.setNavigationPoints(mNavigationPoints);

        mBinding.includeLayoutCommonTitleBar.ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }
}