package com.hitqz.disinfectionrobot.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.gyf.immersionbar.ImmersionBar;
import com.hitqz.disinfectionrobot.R;
import com.hitqz.disinfectionrobot.databinding.ActivityMainBinding;
import com.hitqz.disinfectionrobot.fragment.DeployFragment;
import com.hitqz.disinfectionrobot.fragment.MainFragment;
import com.hitqz.disinfectionrobot.fragment.SettingFragment;
import com.hitqz.disinfectionrobot.i.IGo;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends BaseActivity implements IGo {

    ActivityMainBinding mBinding;
    private MainFragment mMainFragment;
    private DeployFragment mDeployFragment;
    private SettingFragment mSettingFragment;

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
        mBinding.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.page_1:
                        go2Main();
                        break;
                    case R.id.page_2:
                        go2Deploy();
                        break;
                    case R.id.page_3:
                        go2Setting();
                        break;
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
}