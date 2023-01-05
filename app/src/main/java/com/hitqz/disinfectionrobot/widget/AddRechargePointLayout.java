package com.hitqz.disinfectionrobot.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hitqz.disinfectionrobot.databinding.LayoutAddRechargePointBinding;

public class AddRechargePointLayout extends LinearLayout {

    public static final String TAG = AddRechargePointLayout.class.getSimpleName();
    public LayoutAddRechargePointBinding mBinding;

    private View.OnClickListener mAddRechargePointListener;

    public AddRechargePointLayout(Context context) {
        super(context);
        init(context);
    }

    public AddRechargePointLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AddRechargePointLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mBinding = LayoutAddRechargePointBinding.inflate(LayoutInflater.from(context), this, true);
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
    }

    public void setAddRechargePointListener(OnClickListener addRechargePointListener) {
        mAddRechargePointListener = addRechargePointListener;
        mBinding.btnAddRechargePoint.setOnClickListener(addRechargePointListener);
    }
}
