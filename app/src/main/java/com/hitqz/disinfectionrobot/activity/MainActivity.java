package com.hitqz.disinfectionrobot.activity;

import android.os.Bundle;

import com.gyf.immersionbar.ImmersionBar;
import com.hitqz.disinfectionrobot.R;
import com.hitqz.disinfectionrobot.databinding.ActivityMainBinding;
import com.hitqz.disinfectionrobot.i.IGo;

public class MainActivity extends BaseActivity implements IGo {

    ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this)
                .statusBarColor(R.color.transparent).statusBarDarkFont(true).init();
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
    }

    @Override
    public void go2Main() {
//        hideOther();
//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        if (mLoginFragment == null) {
//            mLoginFragment = LoginFragment.newInstance();
//            fragmentTransaction.add(R.id.vp_overlap, mLoginFragment);
//        } else {
//            fragmentTransaction.show(mLoginFragment);
//        }
//
//        fragmentTransaction.commitAllowingStateLoss();
    }
}