package com.hitqz.disinfectionrobot.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.hitqz.disinfectionrobot.data.MapData;
import com.hitqz.disinfectionrobot.databinding.ActivityToggleMapBinding;
import com.hitqz.disinfectionrobot.dialog.MapListDialog;
import com.hitqz.disinfectionrobot.dialog.ToggleMapAlertDialog;
import com.hitqz.disinfectionrobot.util.PathUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@SuppressLint("CheckResult")
public class ToggleMapActivity extends BaseActivity {

    ActivityToggleMapBinding mBinding;
    private List<String> mList;
    private MapData mMapData;

    private void initMap(String mapCode) {
        mMapData = new MapData(PathUtil.getMapPGMFile(getApplicationContext(), mapCode),
                PathUtil.getMapYmlFile(getApplicationContext(), mapCode));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityToggleMapBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mBinding.includeLayoutCommonTitleBar.ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ToggleMapAlertDialog dialog = new ToggleMapAlertDialog();
        dialog.setOnClickListener(new ToggleMapAlertDialog.OnClickListener() {
            @Override
            public void onConfirm() {

            }

            @Override
            public void onCancel() {
                finish();
            }
        });
        dialog.show(getSupportFragmentManager(), ToggleMapAlertDialog.TAG);

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
                        mBinding.tvMapCode.setText("map0622");
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

        mList = new ArrayList<>();
        mList.add("地图1");
        mList.add("地图2");
        mList.add("地图3");
        mList.add("地图4");

        mBinding.btnMapList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapListDialog dialog = new MapListDialog();
                dialog.setList(mList);
                dialog.setOnClickListener(new MapListDialog.OnClickListener() {
                    @Override
                    public void onConfirm(String text) {
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
                                        mBinding.tvMapCode.setText(text);
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
                    }
                });
                dialog.show(getSupportFragmentManager(), dialog.getTag());
            }
        });
    }
}
