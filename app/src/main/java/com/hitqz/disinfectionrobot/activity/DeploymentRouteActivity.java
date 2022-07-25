package com.hitqz.disinfectionrobot.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.hitqz.disinfectionrobot.data.MapData;
import com.hitqz.disinfectionrobot.data.NavigationPoint;
import com.hitqz.disinfectionrobot.databinding.ActivityDeployRouteBinding;
import com.hitqz.disinfectionrobot.dialog.CommonDialog;
import com.hitqz.disinfectionrobot.dialog.DeployAlertDialog;
import com.hitqz.disinfectionrobot.dialog.PointEditDialog;
import com.hitqz.disinfectionrobot.util.AngleUtil;
import com.hitqz.disinfectionrobot.util.PathUtil;
import com.hitqz.disinfectionrobot.widget.NavigationView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DeploymentRouteActivity extends BaseActivity {
    ActivityDeployRouteBinding mBinding;
    private MapData mMapData;
    private final List<NavigationPoint> mNavigationPoints = new ArrayList<>();

    private void initMap(String mapCode) {
        mMapData = new MapData(PathUtil.getMapPGMFile(getApplicationContext(), mapCode),
                PathUtil.getMapYmlFile(getApplicationContext(), mapCode));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityDeployRouteBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
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
                        navigationPoint.mapCode = "mCurrMapCode";
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
