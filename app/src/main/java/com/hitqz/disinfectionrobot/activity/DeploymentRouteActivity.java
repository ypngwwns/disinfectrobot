package com.hitqz.disinfectionrobot.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.hitqz.disinfectionrobot.adapter.NavigationPointAdapter;
import com.hitqz.disinfectionrobot.data.MapDataResponse;
import com.hitqz.disinfectionrobot.data.MapPose;
import com.hitqz.disinfectionrobot.data.NavigationPoint;
import com.hitqz.disinfectionrobot.databinding.ActivityDeployRouteBinding;
import com.hitqz.disinfectionrobot.dialog.CommonDialog;
import com.hitqz.disinfectionrobot.dialog.DeployAlertDialog;
import com.hitqz.disinfectionrobot.dialog.PointEditDialog;
import com.hitqz.disinfectionrobot.net.BaseDataObserver;
import com.hitqz.disinfectionrobot.util.AngleUtil;
import com.hitqz.disinfectionrobot.widget.NavigationView;
import com.sonicers.commonlib.rx.RxSchedulers;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("CheckResult")
public class DeploymentRouteActivity extends BaseActivity {
    private final List<NavigationPoint> mNavigationPoints = new ArrayList<>();
    ActivityDeployRouteBinding mBinding;
    private NavigationPointAdapter mNavigationPointAdapter;
    private MapDataResponse mMapDataResponse;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityDeployRouteBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        showDialog();

        mISkyNet.mapCurGet().compose(RxSchedulers.io_main())
                .subscribeWith(new BaseDataObserver<MapDataResponse>() {
                    @Override
                    public void onSuccess(MapDataResponse model) {
                        if (model != null) {
                            mMapDataResponse = model;
                            Glide.with(DeploymentRouteActivity.this)
                                    .asBitmap()
                                    .load(model.uri)
                                    .into(new SimpleTarget<Bitmap>() {
                                        @Override
                                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                            mMapDataResponse.bitmap = resource;
                                            mBinding.navigationView.setBitmap(mMapDataResponse.bitmap);
                                            mBinding.navigationView.setResolutionAndOrigin(mMapDataResponse.mapResolution, mMapDataResponse.mapOriginx,
                                                    mMapDataResponse.mapOriginy);
                                            getMapPose();
                                            dismissDialog();
                                        }
                                    });
                        } else {
                            dismissDialog();
                        }
                    }

                    @Override
                    public void onFailure(String msg) {
                        dismissDialog();
                        ToastUtils.showShort("获取地图失败");
                    }
                });

        DeployAlertDialog dialog = new DeployAlertDialog();
        dialog.setOnClickListener(new DeployAlertDialog.OnClickListener() {
            @Override
            public void onConfirm() {

            }

            @Override
            public void onCancel() {
                finish();
            }
        });
        dialog.show(getSupportFragmentManager(), DeployAlertDialog.TAG);
        mBinding.includeLayoutCommonTitleBar.ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBinding.navigationView.setOnLongPressListener(new NavigationView.OnLongPressListener() {
            @Override
            public void onLongPress(float x, float y) {
                PointEditDialog dialog = new PointEditDialog();
                dialog.setOnClickListener(new PointEditDialog.OnClickListener() {
                    @Override
                    public void onConfirm(String text, float currentAngle) {
                        NavigationPoint navigationPoint = new NavigationPoint();
                        navigationPoint.mapCode = "map0622";
                        navigationPoint.name = text;
                        navigationPoint.rawX = x;
                        navigationPoint.rawY = y;
                        navigationPoint.radian = AngleUtil.angle2radian(currentAngle);
                        navigationPoint.save();
                        mNavigationPoints.add(navigationPoint);
                        mBinding.navigationView.postInvalidate();
                    }
                });
                dialog.show(getSupportFragmentManager(), dialog.getTag());
            }
        });

        mBinding.navigationView.setNavigationPoints(mNavigationPoints);
        mNavigationPointAdapter = new NavigationPointAdapter(this, mNavigationPoints);
        mBinding.navigationView.setPointAdapter(mNavigationPointAdapter);
        mBinding.npll.setNavigationPointAdapter(mNavigationPointAdapter);
    }

    private void getMapPose() {
        mISkyNet.mapPosListGet().compose(RxSchedulers.io_main())
                .subscribeWith(new BaseDataObserver<List<MapPose>>() {
                    @Override
                    public void onSuccess(List<MapPose> model) {
//                        mBinding.navigationView.setNavigationPoints(model);
                        for (MapPose mapPose : model) {
                            NavigationPoint navigationPoint = new NavigationPoint();
                            navigationPoint.mapCode = mapPose.mapCode;
                            navigationPoint.name = mapPose.name;
                            navigationPoint.rawX = mapPose.posx;
                            navigationPoint.rawY = mapPose.posy;
                            navigationPoint.radian = mapPose.yaw;
                            mNavigationPoints.add(navigationPoint);
                        }
                        mBinding.navigationView.setNavigationPoints(mNavigationPoints);
                        mNavigationPointAdapter.notifyDataSetInvalidated();
                    }

                    @Override
                    public void onFailure(String msg) {
                        ToastUtils.showShort("获取点位列表失败");
                    }
                });
    }

    @Override
    public void onBackPressed() {
        CommonDialog dialog = new CommonDialog();
        dialog.setOnClickListener(new CommonDialog.OnClickListener() {
            @Override
            public void onConfirm() {
                finish();
            }
        });
        dialog.show(getSupportFragmentManager(), dialog.getTag());
    }
}
