package com.hitqz.disinfectionrobot.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.hitqz.disinfectionrobot.R;
import com.hitqz.disinfectionrobot.data.MapArea;
import com.hitqz.disinfectionrobot.databinding.ActivitySetDisinfectAreaBinding;
import com.hitqz.disinfectionrobot.event.RefreshEvent;
import com.hitqz.disinfectionrobot.fragment.DisinfectAreaListFragment;
import com.hitqz.disinfectionrobot.fragment.EditDisinfectAreaFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@SuppressLint("CheckResult")
public class SetDisinfectAreaActivity extends BaseActivity {

    ActivitySetDisinfectAreaBinding mBinding;
    private DisinfectAreaListFragment mDisinfectAreaListFragment;
    private EditDisinfectAreaFragment mEditDisinfectAreaFragment;

    public void go2DisinfectAreaList() {
        hideOther();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (mDisinfectAreaListFragment == null) {
            mDisinfectAreaListFragment = DisinfectAreaListFragment.newInstance();
            fragmentTransaction.add(R.id.vp_content, mDisinfectAreaListFragment);
        } else {
            fragmentTransaction.show(mDisinfectAreaListFragment);
        }

        fragmentTransaction.commitAllowingStateLoss();
    }

    public void go2EditDisinfectArea(MapArea mapArea) {
        hideOther();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (mEditDisinfectAreaFragment == null) {
            mEditDisinfectAreaFragment = EditDisinfectAreaFragment.newInstance();
            fragmentTransaction.add(R.id.vp_content, mEditDisinfectAreaFragment);
        } else {
            fragmentTransaction.show(mEditDisinfectAreaFragment);
        }
        mEditDisinfectAreaFragment.setMapArea(mapArea);

        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivitySetDisinfectAreaBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        go2DisinfectAreaList();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void hideOther() {
        if (mDisinfectAreaListFragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.hide(mDisinfectAreaListFragment);
            fragmentTransaction.commitAllowingStateLoss();
        }

        if (mEditDisinfectAreaFragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.remove(mEditDisinfectAreaFragment);
            mEditDisinfectAreaFragment = null;
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    @Override
    public void onBackPressed() {
        if (mDisinfectAreaListFragment != null && !mDisinfectAreaListFragment.isVisible()) {
            go2DisinfectAreaList();
        } else {
            super.onBackPressed();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void oRefresh(RefreshEvent event) {
        go2DisinfectAreaList();
    }
}
