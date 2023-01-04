package com.hitqz.disinfectionrobot.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.tu.loadingdialog.LoadingDailog;
import com.gyf.immersionbar.ImmersionBar;
import com.hitqz.disinfectionrobot.R;
import com.hitqz.disinfectionrobot.i.IDialog;
import com.hitqz.disinfectionrobot.net.ISkyNet;
import com.hitqz.disinfectionrobot.net.RetrofitManager;
import com.hitqz.disinfectionrobot.singleton.ChassisManager;
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;

@SuppressLint("Registered")
public class BaseActivity extends RxAppCompatActivity implements IDialog {

    /**
     * 屏幕适配
     * 设计图宽960dp
     */
    private static float sNoncompatDensity;
    private static float sNoncompatScaledDensity;
    protected LoadingDailog mLoadingDailog;
    protected ISkyNet mISkyNet;
    protected ChassisManager mChassisManager;

    private static void setCustomDensity(@NonNull Activity activity) {
        Application application = activity.getApplication();
        DisplayMetrics appDisplayMetrics = application.getResources().getDisplayMetrics();
        if (sNoncompatDensity == 0) {
            sNoncompatDensity = appDisplayMetrics.density;
            sNoncompatScaledDensity = appDisplayMetrics.scaledDensity;
            application.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(Configuration newConfig) {
                    if (newConfig != null && newConfig.fontScale > 0) {
                        sNoncompatScaledDensity = application.getResources().getDisplayMetrics().scaledDensity;
                    }
                }

                @Override
                public void onLowMemory() {

                }
            });
        }
        float targetDensity = appDisplayMetrics.widthPixels / 540f;
        float targetScaledDensity = targetDensity * (sNoncompatScaledDensity / sNoncompatDensity);
        int targetDensityDpi = (int) (160 * targetDensity);

        appDisplayMetrics.density = targetDensity;
        appDisplayMetrics.scaledDensity = targetScaledDensity;
        appDisplayMetrics.densityDpi = targetDensityDpi;

        DisplayMetrics activityDisplayMetrics = activity.getResources().getDisplayMetrics();
        activityDisplayMetrics.density = targetDensity;
        activityDisplayMetrics.scaledDensity = targetScaledDensity;
        activityDisplayMetrics.densityDpi = targetDensityDpi;
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this)
                .statusBarColor(R.color.transparent).statusBarDarkFont(true).init();
//        setCustomDensity(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mISkyNet = RetrofitManager.getInstance(this).create(ISkyNet.class);
        mChassisManager = ChassisManager.getInstance(this);
    }

    protected void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.vp_content, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void showDialog() {
        if (mLoadingDailog == null) {
            LoadingDailog.Builder loadBuilder = new LoadingDailog.Builder(this)
                    .setMessage("加载中...")
                    .setCancelable(true)
                    .setCancelOutside(true);
            mLoadingDailog = loadBuilder.create();
        }

        mLoadingDailog.show();
    }

    @Override
    public void dismissDialog() {
        if (mLoadingDailog != null) {
            mLoadingDailog.dismiss();
        }
    }
}
