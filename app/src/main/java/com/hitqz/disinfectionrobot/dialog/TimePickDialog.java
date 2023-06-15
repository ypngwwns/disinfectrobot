package com.hitqz.disinfectionrobot.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.hitqz.disinfectionrobot.R;
import com.hitqz.disinfectionrobot.databinding.DialogTimePickDialogBinding;

@SuppressLint("CheckResult")
public class TimePickDialog extends BaseDialogFragment {

    public static final String TAG = TimePickDialog.class.getSimpleName();

    private DialogTimePickDialogBinding mBinding;
    private OnClickListener mOnClickListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.Theme_AppCompat_Dialog_Alert);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        mBinding = DialogTimePickDialogBinding.inflate(LayoutInflater.from(getContext()));
        mBinding.tpTime.setIs24HourView(true);

        builder.setTitle("确定要执行操作吗")
                .setView(mBinding.getRoot())
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {

                    }
                })
                .setPositiveButton("确定", null)
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

                        if (checkInValid()) return;

                        if (mOnClickListener != null) {
                            mOnClickListener.onConfirm();
                        }

                        dismiss();
                    }
                });
            }
        });

        // Create the AlertDialog object and return it
        return alertDialog;
    }

    private boolean checkInValid() {
        return false;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onConfirm();
    }
}
