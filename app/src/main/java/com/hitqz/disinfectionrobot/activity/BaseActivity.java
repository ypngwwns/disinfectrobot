package com.hitqz.disinfectionrobot.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;

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

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this)
                .statusBarColor(R.color.transparent).statusBarDarkFont(true).init();
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
