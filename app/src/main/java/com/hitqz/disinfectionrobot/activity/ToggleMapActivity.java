package com.hitqz.disinfectionrobot.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.hitqz.disinfectionrobot.data.MapCode;
import com.hitqz.disinfectionrobot.data.MapDataResponse;
import com.hitqz.disinfectionrobot.data.MapListData;
import com.hitqz.disinfectionrobot.databinding.ActivityToggleMapBinding;
import com.hitqz.disinfectionrobot.dialog.MapListDialog;
import com.hitqz.disinfectionrobot.dialog.ToggleMapAlertDialog;
import com.hitqz.disinfectionrobot.net.BaseDataObserver;
import com.sonicers.commonlib.rx.RxSchedulers;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 切换地图
 */
@SuppressLint("CheckResult")
public class ToggleMapActivity extends BaseActivity {

    private final List<String> mList = new ArrayList<>();
    ActivityToggleMapBinding mBinding;
    private MapDataResponse mMapDataResponse;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityToggleMapBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mBinding.includeLayoutCommonTitleBar.vpBackContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ToggleMapAlertDialog dialog = new ToggleMapAlertDialog();
        dialog.setOnClickListener(new ToggleMapAlertDialog.OnClickListener() {
            @Override
            public void onConfirm() {
                init();
            }

            @Override
            public void onCancel() {
                finish();
            }
        });
        dialog.show(getSupportFragmentManager(), ToggleMapAlertDialog.TAG);
    }

    /**
     * 加载当前地图
     */
    private void init() {
        loadCurrentMap();
        mBinding.btnMapList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog();

                mISkyNet.mapListGet().compose(RxSchedulers.io_main())
                        .subscribeWith(new BaseDataObserver<MapListData>() {
                            @Override
                            public void onSuccess(MapListData model) {
                                dismissDialog();
                                if (model != null && model.mapNameList != null && model.mapNameList.size() > 0) {
                                    mList.clear();
                                    mList.addAll(model.mapNameList);
                                    MapListDialog dialog = new MapListDialog();
                                    dialog.setList(mList);
                                    dialog.setOnClickListener(new MapListDialog.OnClickListener() {
                                        @Override
                                        public void onConfirm(String text) {
                                            if (mMapDataResponse.mapName.equals(text)) {
                                                ToastUtils.showShort("请选择另一张地图");
                                                return;
                                            }
                                            showDialog();
                                            MapCode mapCode = new MapCode();
                                            mapCode.mapCode = text;
                                            mISkyNet.navSwitchMap(mapCode).compose(RxSchedulers.io_main())
                                                    .subscribeWith(new BaseDataObserver<Object>() {
                                                        @Override
                                                        public void onSuccess(Object model) {
                                                            ToastUtils.showShort("切换地图成功");
                                                            mChassisManager.mMapDataResponse = null;
                                                            loadCurrentMap();
                                                            dismissDialog();
                                                        }

                                                        @Override
                                                        public void onFailure(String msg) {
                                                            ToastUtils.showShort("切换地图失败%s:", msg);
                                                            dismissDialog();
                                                        }
                                                    });
                                        }
                                    });
                                    dialog.show(getSupportFragmentManager(), dialog.getTag());
                                }
                            }

                            @Override
                            public void onFailure(String msg) {
                                ToastUtils.showShort("获取地图列表失败%s:", msg);
                                dismissDialog();
                            }
                        });
            }
        });
    }

    /**
     * 加载当前地图
     */
    private void loadCurrentMap() {
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
}
