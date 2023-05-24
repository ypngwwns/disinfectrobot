package com.hitqz.disinfectionrobot.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.hitqz.disinfectionrobot.R;

@SuppressLint("CheckResult")
public class DeployAlertDialog extends BaseDialogFragment {

    public static final String TAG = DeployAlertDialog.class.getSimpleName();

    //    private DialogDeployAlertBinding mBinding;
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
//        mBinding = DialogDeployAlertBinding.inflate(LayoutInflater.from(getContext()));

        builder.setTitle("将进入部署模式")
                .setMessage("进入部署模式会暂停使用其它功能，退出部署模式自动恢复")
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {

                    }
                })
                .setPositiveButton("确定", null)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if (mOnClickListener != null) {
                            mOnClickListener.onCancel();
                        }

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

        void onCancel();
    }
}
