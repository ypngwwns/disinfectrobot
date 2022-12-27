package com.hitqz.disinfectionrobot.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.hitqz.disinfectionrobot.R;
import com.hitqz.disinfectionrobot.data.Task;
import com.hitqz.disinfectionrobot.databinding.ActivityDisinfectRegularlyBinding;
import com.hitqz.disinfectionrobot.fragment.DisinfectRegularlyFragment;
import com.hitqz.disinfectionrobot.fragment.EditTasksFragment;

public class DisinfectRegularlyActivity extends BaseActivity {
    ActivityDisinfectRegularlyBinding mBinding;
    private DisinfectRegularlyFragment mDisinfectRegularlyFragment;
    private EditTasksFragment mEditTasksFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityDisinfectRegularlyBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        go2DisinfectRegularly();
    }

    public void go2DisinfectRegularly() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (mDisinfectRegularlyFragment == null) {
            mDisinfectRegularlyFragment = DisinfectRegularlyFragment.newInstance();
            fragmentTransaction.add(R.id.vp_content, mDisinfectRegularlyFragment, DisinfectRegularlyFragment.TAG);
        } else {
            fragmentTransaction.show(mDisinfectRegularlyFragment);
        }

        fragmentTransaction.commitAllowingStateLoss();
    }

    public void go2EditTask(Task task) {
        hideOther();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (mEditTasksFragment == null) {
            mEditTasksFragment = EditTasksFragment.newInstance();
            mEditTasksFragment.setTask(task);
            fragmentTransaction.add(R.id.vp_content, mEditTasksFragment, EditTasksFragment.TAG);
            fragmentTransaction.addToBackStack(null);//后退时先移除，不销毁Activity
        } else {
            fragmentTransaction.show(mEditTasksFragment);
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

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        int backCount = fragmentManager.getBackStackEntryCount();
        if (backCount == 0) {
            finish();
        } else if (backCount == 1) {
            go2DisinfectRegularly();
            super.onBackPressed();
            mEditTasksFragment = null;
        }
    }
}
