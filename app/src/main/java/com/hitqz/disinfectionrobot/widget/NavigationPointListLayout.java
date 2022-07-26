package com.hitqz.disinfectionrobot.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hitqz.disinfectionrobot.adapter.NavigationPointAdapter;
import com.hitqz.disinfectionrobot.databinding.LayoutNavigationPointListBinding;

public class NavigationPointListLayout extends LinearLayout {

    public static final String TAG = NavigationPointListLayout.class.getSimpleName();
    private LayoutNavigationPointListBinding mBinding;
    private NavigationPointAdapter mNavigationPointAdapter;

    private INavigationPointListListener mListener;

    public NavigationPointListLayout(Context context) {
        super(context);
        init(context);
    }

    public NavigationPointListLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NavigationPointListLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mBinding = LayoutNavigationPointListBinding.inflate(LayoutInflater.from(context), this, true);
    }

    public void setListener(INavigationPointListListener listener) {
        this.mListener = listener;
    }

    public void setNavigationPointAdapter(NavigationPointAdapter navigationPointAdapter) {
        mNavigationPointAdapter = navigationPointAdapter;
        mBinding.lvNavigationPoint.setAdapter(mNavigationPointAdapter);
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
    }

    public interface INavigationPointListListener {
        void onClickEdit();

        void onClickClose();
    }
}
