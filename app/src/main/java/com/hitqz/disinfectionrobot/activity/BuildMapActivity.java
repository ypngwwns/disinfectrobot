package com.hitqz.disinfectionrobot.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hitqz.disinfectionrobot.constant.TokenKeys;
import com.hitqz.disinfectionrobot.data.MapBuildRequest;
import com.hitqz.disinfectionrobot.data.MapUploadRequest;
import com.hitqz.disinfectionrobot.databinding.ActivityBuildMapBinding;
import com.hitqz.disinfectionrobot.dialog.CommonDialog;
import com.hitqz.disinfectionrobot.dialog.SaveMapDialog;
import com.hitqz.disinfectionrobot.net.BaseDataObserver;
import com.sonicers.commonlib.rx.RxSchedulers;

@SuppressLint("CheckResult")
public class BuildMapActivity extends BaseActivity {

    ActivityBuildMapBinding mBinding;
    private boolean mBuildingMap = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityBuildMapBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        setListener();
        mBinding.btnMapBuild.setText(mBuildingMap ? "保存地图" : "开始建图");
    }

    private void setListener() {

        mBinding.btnMapBuild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBuildingMap) {
                    SaveMapDialog dialog = new SaveMapDialog();
                    dialog.setOnClickListener(new SaveMapDialog.OnClickListener() {
                        @Override
                        public void onConfirm(String text) {
                            showDialog();
                            String token = SPUtils.getInstance().getString(TokenKeys.token);
                            MapUploadRequest request = new MapUploadRequest(token, text);
                            mISkyNet.map_upload(request).compose(RxSchedulers.io_main())
                                    .subscribeWith(new BaseDataObserver<Object>() {
                                        @Override
                                        public void onSuccess(Object model) {
                                            dismissDialog();
                                            ToastUtils.showShort("保存地图成功");
                                        }

                                        @Override
                                        public void onFailure(String msg) {
                                            dismissDialog();
                                            ToastUtils.showShort("保存地图失败:" + msg);
                                        }
                                    });
                        }
                    });
                    dialog.show(getSupportFragmentManager(), SaveMapDialog.TAG);
                } else {
                    showDialog();
                    String token = SPUtils.getInstance().getString(TokenKeys.token);
                    MapBuildRequest request = new MapBuildRequest(token, 0);
                    mISkyNet.map_build(request).compose(RxSchedulers.io_main())
                            .subscribeWith(new BaseDataObserver<Object>() {
                                @Override
                                public void onSuccess(Object model) {
                                    dismissDialog();
                                    ToastUtils.showShort("开始建图成功");
                                }

                                @Override
                                public void onFailure(String msg) {
                                    dismissDialog();
                                    ToastUtils.showShort("开始建图失败:" + msg);
                                }
                            });
                }
            }
        });
        mBinding.includeLayoutCommonTitleBar.ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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
