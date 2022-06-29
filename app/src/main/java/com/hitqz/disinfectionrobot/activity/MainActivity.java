package com.hitqz.disinfectionrobot.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;

import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.gyf.immersionbar.ImmersionBar;
import com.hitqz.disinfectionrobot.R;
import com.hitqz.disinfectionrobot.databinding.ActivityMainBinding;
import com.hitqz.disinfectionrobot.fragment.DeployFragment;
import com.hitqz.disinfectionrobot.fragment.MainFragment;
import com.hitqz.disinfectionrobot.fragment.SettingFragment;
import com.hitqz.disinfectionrobot.i.IGo;

public class MainActivity extends BaseActivity implements IGo {

    ActivityMainBinding mBinding;
    private MainFragment mMainFragment;
    private DeployFragment mDeployFragment;
    private SettingFragment mSettingFragment;
    private int mBackPressCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this)
                .statusBarColor(R.color.transparent).statusBarDarkFont(true).init();
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        go2Main();
        setListener();
    }

    private void setListener() {
        mBinding.bottomNavigation.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.page_1) {
                    go2Main();
                } else if (item.getItemId() == R.id.page_2) {
                    go2Deploy();
                } else if (item.getItemId() == R.id.page_3) {
                    go2Setting();
                }
                return true;
            }
        });
    }

    @Override
    public void go2Main() {
        hideOther();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (mMainFragment == null) {
            mMainFragment = MainFragment.newInstance();
            fragmentTransaction.add(R.id.vp_content, mMainFragment);
        } else {
            fragmentTransaction.show(mMainFragment);
        }

        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void go2Deploy() {
        hideOther();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (mDeployFragment == null) {
            mDeployFragment = DeployFragment.newInstance();
            fragmentTransaction.add(R.id.vp_content, mDeployFragment);
        } else {
            fragmentTransaction.show(mDeployFragment);
        }

        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void go2Setting() {
        hideOther();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (mSettingFragment == null) {
            mSettingFragment = SettingFragment.newInstance();
            fragmentTransaction.add(R.id.vp_content, mSettingFragment);
        } else {
            fragmentTransaction.show(mSettingFragment);
        }

        fragmentTransaction.commitAllowingStateLoss();
    }

    private void hideOther() {
        if (mMainFragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.hide(mMainFragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
        if (mDeployFragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.hide(mDeployFragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
        if (mSettingFragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.hide(mSettingFragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    @Override
    public void onBackPressed() {
        mBackPressCount++;
        if (mBackPressCount == 1) {
            ToastUtils.showShort("再按一次退出");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBackPressCount = 0;
                }
            }, 1500); // 延时1.5秒清空
        } else if (mBackPressCount >= 2) {
            finish();
        }
    }
}