package com.hitqz.disinfectionrobot.net;

import android.content.Context;
import android.util.Log;

import com.blankj.utilcode.util.SPUtils;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.hitqz.disinfectionrobot.DisinfectRobotApplication;
import com.hitqz.disinfectionrobot.constant.TokenKeys;
import com.jeremy.retrofitmock.SimpleMockInterceptor;
import com.sonicers.commonlib.net.HttpCommonInterceptor;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {

    public static final String TAG = "RetrofitManager";

    public static final String SEVER_URL = "http://192.168.3.245:18080";

    private static final int DEFAULT_TIME_OUT = 5;//超时时间 5s
    private static final int DEFAULT_READ_TIME_OUT = 10;
    private static RetrofitManager mManager;
    private Retrofit mRetrofit;

    private RetrofitManager(Context context) {
        if (mRetrofit == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS);//连接超时时间
            builder.writeTimeout(DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS);//写操作 超时时间
            builder.readTimeout(DEFAULT_READ_TIME_OUT, TimeUnit.SECONDS);//读操作超时时间
            // 添加公共参数拦截器
            HttpCommonInterceptor commonInterceptor = new HttpCommonInterceptor.Builder()
                    .addHeaderParams("Content-Type", "application/json")
                    .build();
            builder.addInterceptor(commonInterceptor);
            builder.addNetworkInterceptor(new TokenHeaderInterceptor());
            ClearableCookieJar cookieJar =
                    new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(DisinfectRobotApplication.instance.getApplicationContext()));

            builder.cookieJar(cookieJar);
//            // if (BuildConfig.DEBUG) {
//            // Log信息拦截器
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLogger());
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            //设置 Debug Log 模式
            builder.addNetworkInterceptor(loggingInterceptor);
            builder.addInterceptor(new SimpleMockInterceptor(false));
//            //}

            // 创建Retrofit
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(SEVER_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(builder.build())
                    .build();
        }
    }

    /**
     * 获取RetrofitManager
     *
     * @return RetrofitManager
     */
    public static RetrofitManager getInstance(Context context) {
        if (mManager == null) {
            synchronized (RetrofitManager.class) {
                if (mManager == null) {
                    mManager = new RetrofitManager(context);
                }
            }
        }
        return mManager;
    }

    /**
     * 获取对应的Service
     *
     * @param service Service 的 class
     * @param <T>
     * @return
     */
    public <T> T create(Class<T> service) {
        return mRetrofit.create(service);
    }

    //在请求头里添加token的拦截器处理
    public static class TokenHeaderInterceptor implements Interceptor {
        @NotNull
        @Override
        public Response intercept(@NotNull Chain chain) throws IOException {

            long expireTime = SPUtils.getInstance().getLong(TokenKeys.expiresIn, 0);

            if (System.currentTimeMillis() > expireTime) {
                Request originalRequest = chain.request();
                return chain.proceed(originalRequest);
            } else {
                Request originalRequest = chain.request();
                //key的话以后台给的为准，我这边是叫token
                Request updateRequest = originalRequest.newBuilder().addHeader("Authorization", SPUtils.getInstance().getString(TokenKeys.tokenHead)
                        + SPUtils.getInstance().getString(TokenKeys.token)).build();
                return chain.proceed(updateRequest);
            }
        }
    }

    private class HttpLogger implements HttpLoggingInterceptor.Logger {

        @Override
        public void log(String message) {
            Log.i(TAG, message);
        }
    }
}
