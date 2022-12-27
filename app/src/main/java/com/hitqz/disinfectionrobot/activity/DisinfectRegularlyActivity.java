package com.hitqz.disinfectionrobot.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.hitqz.disinfectionrobot.R;
import com.hitqz.disinfectionrobot.data.Task;
import com.hitqz.disinfectionrobot.databinding.ActivityDisinfectRegularlyBinding;
import com.hitqz.disinfectionrobot.event.TaskRefreshEvent;
import com.hitqz.disinfectionrobot.fragment.DisinfectRegularlyFragment;
import com.hitqz.disinfectionrobot.fragment.EditTasksFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void go2DisinfectRegularly() {
        hideOther();
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
//            fragmentTransaction.addToBackStack(null);//后退时先移除，不销毁Activity
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
        if (mEditTasksFragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.remove(mEditTasksFragment);
            mEditTasksFragment = null;
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    @Override
    public void onBackPressed() {
        if (mDisinfectRegularlyFragment != null && !mDisinfectRegularlyFragment.isVisible()) {
            go2DisinfectRegularly();
        } else {
            super.onBackPressed();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void oRefresh(TaskRefreshEvent event) {
        go2DisinfectRegularly();
    }
}
