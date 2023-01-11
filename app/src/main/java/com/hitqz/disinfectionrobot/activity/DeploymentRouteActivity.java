package com.hitqz.disinfectionrobot.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.hitqz.disinfectionrobot.DisinfectRobotApplication;
import com.hitqz.disinfectionrobot.adapter.NavigationPointAdapter;
import com.hitqz.disinfectionrobot.data.MapDataResponse;
import com.hitqz.disinfectionrobot.data.MapPose;
import com.hitqz.disinfectionrobot.data.NavigationPoint;
import com.hitqz.disinfectionrobot.data.PointData;
import com.hitqz.disinfectionrobot.data.RobotStatus;
import com.hitqz.disinfectionrobot.databinding.ActivityDeployRouteBinding;
import com.hitqz.disinfectionrobot.dialog.CommonDialog;
import com.hitqz.disinfectionrobot.dialog.DeployAlertDialog;
import com.hitqz.disinfectionrobot.dialog.PointEditDialog;
import com.hitqz.disinfectionrobot.net.BaseDataObserver;
import com.hitqz.disinfectionrobot.net.ws.JWebSocketClientService;
import com.hitqz.disinfectionrobot.util.AngleUtil;
import com.hitqz.disinfectionrobot.widget.NavigationView;
import com.sonicers.commonlib.rx.RxSchedulers;
import com.sonicers.commonlib.singleton.GsonUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@SuppressLint("CheckResult")
public class DeploymentRouteActivity extends BaseActivity {
    public static final String TAG = "DeploymentRouteActivity";

    private final List<NavigationPoint> mNavigationPoints = new ArrayList<>();
    ActivityDeployRouteBinding mBinding;
    private NavigationPoint mRechargePos;
    private NavigationPointAdapter mNavigationPointAdapter;
    private MapDataResponse mMapDataResponse;

    private NavigationPoint mRobotPos = new NavigationPoint();

    private WebSocketMessageReceiver mWebSocketMessageReceiver;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DisinfectRobotApplication.instance.removeWebSocketCallback(mWebSocketMessageReceiver);
    }

    /**
     * 动态注册广播
     */
    private void doRegisterReceiver() {
        mWebSocketMessageReceiver = new WebSocketMessageReceiver(this);
        DisinfectRobotApplication.instance.addWebSocketCallback(mWebSocketMessageReceiver);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityDeployRouteBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
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
        mBinding.includeLayoutCommonTitleBar.vpBackContainer.setOnClickListener(new View.OnClickListener() {
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
        mNavigationPointAdapter.setDeleteClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                CommonDialog deleteConfirmDialog = new CommonDialog();
                deleteConfirmDialog.setOnClickListener(new CommonDialog.OnClickListener() {
                    @Override
                    public void onConfirm() {
                        mISkyNet.deleteById(mNavigationPoints.get(position).id).compose(RxSchedulers.io_main())
                                .subscribeWith(new BaseDataObserver<Object>() {
                                    @Override
                                    public void onSuccess(Object model) {
                                        mNavigationPoints.remove(position);
                                        mNavigationPointAdapter.notifyDataSetChanged();
                                        mBinding.navigationView.setNavigationPoints(mNavigationPoints);
                                        mBinding.navigationView.postInvalidate();
                                        dismissDialog();
                                    }

                                    @Override
                                    public void onFailure(String msg) {
                                        ToastUtils.showShort("删除点位失败:%s", msg);
                                        dismissDialog();
                                    }
                                });
                    }
                });
                deleteConfirmDialog.show(getSupportFragmentManager(), CommonDialog.TAG);
            }
        });
        mBinding.navigationView.setPointAdapter(mNavigationPointAdapter);
        mBinding.npll.setNavigationPointAdapter(mNavigationPointAdapter);
        mBinding.npll.setAddNavigationPointListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
                PointData pointData = new PointData("导航点" + System.currentTimeMillis(), "1");
                mISkyNet.addMapPos(pointData).compose(RxSchedulers.io_main())
                        .subscribeWith(new BaseDataObserver<MapPose>() {
                            @Override
                            public void onSuccess(MapPose model) {
                                mNavigationPoints.add(NavigationPoint.convertFromMapPose(model));
                                mNavigationPointAdapter.notifyDataSetChanged();
                                mBinding.navigationView.setNavigationPoints(mNavigationPoints);
                                mBinding.navigationView.postInvalidate();
                                dismissDialog();
                            }

                            @Override
                            public void onFailure(String msg) {
                                ToastUtils.showShort("添加导航点位失败:%s", msg);
                                dismissDialog();
                            }
                        });
            }
        });
        mBinding.arpl.setAddRechargePointListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
                PointData pointData = new PointData("充电点" + System.currentTimeMillis(), "2");
                mISkyNet.addMapPos(pointData).compose(RxSchedulers.io_main())
                        .subscribeWith(new BaseDataObserver<MapPose>() {
                            @Override
                            public void onSuccess(MapPose model) {
                                NavigationPoint navigationPoint = NavigationPoint.convertFromMapPose(model);
                                mBinding.navigationView.setRechargePos(navigationPoint);
                                mBinding.navigationView.postInvalidate();
                                mBinding.npll.setVisibility(View.VISIBLE);
                                dismissDialog();
                            }

                            @Override
                            public void onFailure(String msg) {
                                ToastUtils.showShort("添加充电点失败:%s", msg);
                                mBinding.npll.setVisibility(View.VISIBLE);
                                dismissDialog();
                            }
                        });
            }
        });
        DisinfectRobotApplication.instance.jWebSClientService.sendMsg("{\"topic\": \"ROBOT_STATUS_GET\"}");
        doRegisterReceiver();
    }

    private void getMapPose() {
        showDialog();
        mISkyNet.mapPosListGet().compose(RxSchedulers.io_main())
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
                        mNavigationPointAdapter.notifyDataSetInvalidated();

                        if (mRechargePos == null) {
                            mBinding.arpl.mBinding.btnIgnore.setVisibility(View.GONE);
                        } else {
                            mBinding.arpl.mBinding.btnIgnore.setVisibility(View.VISIBLE);
                            mBinding.arpl.mBinding.btnIgnore.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mBinding.npll.setVisibility(View.VISIBLE);
                                    mBinding.arpl.setVisibility(View.GONE);
                                }
                            });
                        }
                        dismissDialog();
                    }

                    @Override
                    public void onFailure(String msg) {
                        ToastUtils.showShort("获取点位列表失败");
                        dismissDialog();
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

    private static class WebSocketMessageReceiver implements JWebSocketClientService.WebSocketCallback {
        private DeploymentRouteActivity mActivity;

        public WebSocketMessageReceiver(DeploymentRouteActivity activity) {
            mActivity = activity;
        }

        @Override
        public void onMessage(String message) {
            RobotStatus robotStatus = GsonUtil.getInstance().fromJson(message, RobotStatus.class);
            if (robotStatus == null || robotStatus.getLaserData() == null) {
                return;
            }
            mActivity.mRobotPos.rawX = robotStatus.getCurrentPos().getX();
            mActivity.mRobotPos.rawY = robotStatus.getCurrentPos().getY();
            mActivity.mRobotPos.radian = robotStatus.getCurrentPos().getYaw();
            mActivity.mBinding.navigationView.setRobotPoint(mActivity.mRobotPos);
            mActivity.mBinding.navigationView.setLaserScan(robotStatus.getLaserOriginData());
            mActivity.mBinding.navigationView.postInvalidate();
        }

        @Override
        public void onConnectSuccess(String s) {

        }
    }
}
