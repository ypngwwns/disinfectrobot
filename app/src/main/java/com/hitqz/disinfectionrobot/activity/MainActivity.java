package com.hitqz.disinfectionrobot.activity;

import android.os.Bundle;

import androidx.fragment.app.FragmentTransaction;

import com.gyf.immersionbar.ImmersionBar;
import com.hitqz.disinfectionrobot.R;
import com.hitqz.disinfectionrobot.databinding.ActivityMainBinding;
import com.hitqz.disinfectionrobot.fragment.MainFragment;
import com.hitqz.disinfectionrobot.i.IGo;

public class MainActivity extends BaseActivity implements IGo {

    ActivityMainBinding mBinding;
    private MainFragment mMainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this)
                .statusBarColor(R.color.transparent).statusBarDarkFont(true).init();
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        go2Main();
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

    private void hideOther() {

    }
}