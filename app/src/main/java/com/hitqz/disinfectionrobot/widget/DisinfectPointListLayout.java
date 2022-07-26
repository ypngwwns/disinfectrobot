package com.hitqz.disinfectionrobot.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hitqz.disinfectionrobot.adapter.DisinfectPointAdapter;
import com.hitqz.disinfectionrobot.databinding.LayoutDisinfectPointListBinding;

public class DisinfectPointListLayout extends LinearLayout {

    public static final String TAG = DisinfectPointListLayout.class.getSimpleName();
    private LayoutDisinfectPointListBinding mBinding;
    private DisinfectPointAdapter mDisinfectPointAdapter;

    private INavigationPointListListener mListener;

    public DisinfectPointListLayout(Context context) {
        super(context);
        init(context);
    }

    public DisinfectPointListLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DisinfectPointListLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mBinding = LayoutDisinfectPointListBinding.inflate(LayoutInflater.from(context), this, true);
        mBinding.dlvNavigationPoint.setDragType(true);
    }

    public void setListener(INavigationPointListListener listener) {
        this.mListener = listener;
    }

    public void setNavigationPointAdapter(DisinfectPointAdapter disinfectPointAdapter) {
        mDisinfectPointAdapter = disinfectPointAdapter;
        mBinding.dlvNavigationPoint.setAdapter(mDisinfectPointAdapter);
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
    }

    public void setName(String name) {
        mBinding.tvMapAreaName.setText(name);
    }

    public interface INavigationPointListListener {
        void onClickEdit();

        void onClickClose();
    }
}
