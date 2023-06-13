package com.hitqz.disinfectionrobot.dialog;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.hitqz.disinfectionrobot.activity.BaseActivity;

import me.jessyan.autosize.AutoSize;

public class BaseDialogFragment extends DialogFragment {

    protected BaseActivity mBaseActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() instanceof BaseActivity) {
            mBaseActivity = (BaseActivity) getActivity();
        }
        AutoSize.autoConvertDensity(mBaseActivity, 540f, true);
    }
}
