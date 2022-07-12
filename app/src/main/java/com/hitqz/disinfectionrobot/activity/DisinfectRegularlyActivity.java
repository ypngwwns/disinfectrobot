package com.hitqz.disinfectionrobot.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.hitqz.disinfectionrobot.R;
import com.hitqz.disinfectionrobot.databinding.ActivityDisinfectRegularlyBinding;
import com.hitqz.disinfectionrobot.fragment.DisinfectRegularlyFragment;

public class DisinfectRegularlyActivity extends BaseActivity {
    ActivityDisinfectRegularlyBinding mBinding;
    private DisinfectRegularlyFragment mDisinfectRegularlyFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityDisinfectRegularlyBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        go2DisinfectRegularly();
    }

    public void go2DisinfectRegularly() {
        hideOther();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (mDisinfectRegularlyFragment == null) {
            mDisinfectRegularlyFragment = DisinfectRegularlyFragment.newInstance();
            fragmentTransaction.add(R.id.vp_content, mDisinfectRegularlyFragment);
        } else {
            fragmentTransaction.show(mDisinfectRegularlyFragment);
        }

        fragmentTransaction.commitAllowingStateLoss();
    }

    private void hideOther() {
        if (mDisinfectRegularlyFragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.hide(mDisinfectRegularlyFragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }
}
