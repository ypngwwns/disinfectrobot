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

    private boolean editing = false;
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

    public boolean isEditing() {
        return editing;
    }

    private void init(Context context) {
        mBinding = LayoutNavigationPointListBinding.inflate(LayoutInflater.from(context), this, true);

        mBinding.getCurPoint.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                NavigationActivity navigationActivity = (NavigationActivity) getContext();
//                AddCurrentPositionDialog dialog = new AddCurrentPositionDialog();
//                dialog.setOnClickListener(navigationActivity::addCurrentPosition);
//                dialog.show(navigationActivity.getSupportFragmentManager(), dialog.getTag());
            }
        });

        mBinding.editPointList.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (mBinding.editPointList.isActivated()) {
//                    mBinding.editPointList.setActivated(false);
//                    mBinding.navigationPointList.setDragType(false);
//                    editing = false;
//                    return;
//                }
//                editing = true;
//                mBinding.editPointList.setActivated(true);
//                mBinding.navigationPointList.setDragType(true);
            }
        });
        mBinding.closePointList.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setVisibility(View.GONE);
//                selectPosReset();
//                mNavigationView.setCurrentMode(NavigationView.ViewOpeModeEum.NORMAL_MODE);
            }
        });
    }

    public void setListener(INavigationPointListListener listener) {
        this.mListener = listener;
    }

    public void setNavigationPointAdapter(NavigationPointAdapter navigationPointAdapter) {
        mNavigationPointAdapter = navigationPointAdapter;
        mBinding.navigationPointList.setAdapter(mNavigationPointAdapter);
    }

    public void selectPosReset() {
        mNavigationPointAdapter.setSelectedPos(-1);
        mNavigationPointAdapter.notifyDataSetInvalidated();
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
//        if (visibility == GONE || visibility == INVISIBLE) {
//            selectPosReset();
//        }
    }

    public interface INavigationPointListListener {
        void onClickEdit();

        void onClickClose();
    }
}
