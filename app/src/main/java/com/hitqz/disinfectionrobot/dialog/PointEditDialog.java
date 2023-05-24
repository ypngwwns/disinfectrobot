package com.hitqz.disinfectionrobot.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.NumberKeyListener;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.hitqz.disinfectionrobot.R;
import com.hitqz.disinfectionrobot.databinding.DialogPointEditorBinding;

@SuppressLint("CheckResult")
public class PointEditDialog extends BaseDialogFragment {

    public static final String TAG = PointEditDialog.class.getSimpleName();

    private DialogPointEditorBinding mBinding;
    private OnClickListener mOnClickListener;
    private String mInitName;
    private float mInitAngle;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.Theme_AppCompat_Dialog_Alert);
    }

    public void setPointInfo(String name, float angle) {
        mInitName = name;
        mInitAngle = angle;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        mBinding = DialogPointEditorBinding.inflate(LayoutInflater.from(getContext()));
        String title = "请设置点位名称和角度";

        if (!TextUtils.isEmpty(mInitName)) {
            if ("init_pos".equals(mInitName)) {
                mBinding.et1.setVisibility(View.GONE);
                title = "请设置初始位置角度";
            } else if ("recharge_pos".equals(mInitName)) {
                mBinding.et1.setVisibility(View.GONE);
                title = "请设置充电位置角度";
            } else {
                mBinding.et1.setText(mInitName);
            }
        }
        // 限制输入长度
        mBinding.et1.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(254)
        });
        // 限制输入字符
        mBinding.et1.setKeyListener(new NumberKeyListener() {
            @NonNull
            @Override
            protected char[] getAcceptedChars() {
                return "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_-".toCharArray();
            }

            @Override
            public int getInputType() {
                return InputType.TYPE_CLASS_TEXT;
            }
        });

        mBinding.pointAngleView.setCurrentAngle(mInitAngle);
        mBinding.pointAngleView.setFocusable(true);
        mBinding.pointAngleView.setFocusableInTouchMode(true);
        mBinding.pointAngleView.requestFocus();

        builder.setTitle(title)
                .setView(mBinding.getRoot())
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (mOnClickListener != null) {
                            mOnClickListener.onConfirm(String.valueOf(mBinding.et1.getText()), mBinding.pointAngleView.getCurrentAngle());
                        }
                        dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                //重写“确定”，截取监听
                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

//                        if (checkInValid()) return;

                        if (mOnClickListener != null) {
                            mOnClickListener.onConfirm(String.valueOf(mBinding.et1.getText()), mBinding.pointAngleView.getCurrentAngle());
                        }

                        dismiss();
                    }
                });
            }
        });

        // Create the AlertDialog object and return it
        return alertDialog;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onConfirm(String text, float currentAngle);
    }
}
