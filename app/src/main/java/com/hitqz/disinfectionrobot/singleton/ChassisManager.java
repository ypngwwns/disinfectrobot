package com.hitqz.disinfectionrobot.singleton;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.hitqz.disinfectionrobot.data.MapDataResponse;
import com.hitqz.disinfectionrobot.net.BaseDataObserver;
import com.hitqz.disinfectionrobot.net.ISkyNet;
import com.hitqz.disinfectionrobot.net.RetrofitManager;
import com.sonicers.commonlib.rx.RxSchedulers;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import io.reactivex.observers.DisposableObserver;
import okhttp3.ResponseBody;

@SuppressLint("CheckResult")
public class ChassisManager {
    private static volatile ChassisManager mSingleton;
    public MapDataResponse mMapDataResponse;
    private Context mContext;
    private ISkyNet mISkyNet;

    private CountDownLatch mCountDownLatch;

    private ChassisManager(Context context) {
        mContext = context.getApplicationContext();
        mISkyNet = RetrofitManager.getInstance(mContext).create(ISkyNet.class);
    }

    public static ChassisManager getInstance(Context context) {
        if (mSingleton == null) {
            synchronized (ChassisManager.class) {
                if (mSingleton == null) {
                    mSingleton = new ChassisManager(context);
                }
            }
        }

        return mSingleton;
    }

    /**
     * 初始化地图数据，请在子线程调用
     */
    public void loadChassisData() {
        mCountDownLatch = new CountDownLatch(1);
        mMapDataResponse = null;
        mISkyNet.mapCurGet().compose(RxSchedulers.io_main())
                .subscribeWith(new BaseDataObserver<MapDataResponse>() {
                    @Override
                    public void onSuccess(MapDataResponse model) {
                        if (model != null) {
                            mMapDataResponse = model;

                            mISkyNet.downloadFile(model.uri)
                                    .compose(RxSchedulers.io_main())
                                    .subscribeWith(new DisposableObserver<ResponseBody>() {
                                        @Override
                                        public void onNext(ResponseBody responseBody) {
                                            // 将ResponseBody的字节流转换为byte数组
                                            try {
                                                byte[] bytes = responseBody.bytes();
                                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                                mMapDataResponse.bitmap = bitmap;
                                            } catch (IOException e) {
                                                throw new RuntimeException(e);
                                            }
                                            mCountDownLatch.countDown();
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            mCountDownLatch.countDown();
                                        }

                                        @Override
                                        public void onComplete() {

                                        }
                                    });
//                            Glide.with(mContext)
//                                    .asBitmap()
//                                    .load(model.uri)
//                                    .into(new SimpleTarget<Bitmap>() {
//                                        @Override
//                                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                                            mMapDataResponse.bitmap = resource;
//                                            mCountDownLatch.countDown();
//                                        }
//                                    });
                        } else {
                            mCountDownLatch.countDown();
                        }
                    }

                    @Override
                    public void onFailure(String msg) {
                        ToastUtils.showShort("获取地图失败");
                        mCountDownLatch.countDown();
                    }
                });
        try {
            mCountDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
