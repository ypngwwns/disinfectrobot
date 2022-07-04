package com.hitqz.disinfectionrobot.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.NumberKeyListener;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.hitqz.disinfectionrobot.R;
import com.hitqz.disinfectionrobot.databinding.DialogSaveMapBinding;

@SuppressLint("CheckResult")
public class SaveMapDialog extends DialogFragment {

    public static final String TAG = SaveMapDialog.class.getSimpleName();

    private DialogSaveMapBinding mBinding;
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
        mBinding = DialogSaveMapBinding.inflate(LayoutInflater.from(getContext()));
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

        builder.setTitle("请输入地图名称")
                .setView(mBinding.getRoot())
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (mOnClickListener != null) {
                            mOnClickListener.onConfirm(String.valueOf(mBinding.et1.getText()));
                        }
                        dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                    }
                });

        // Create the AlertDialog object and return it
        return builder.create();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onConfirm(String text);
    }
}
