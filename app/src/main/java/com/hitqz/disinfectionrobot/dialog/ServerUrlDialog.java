package com.hitqz.disinfectionrobot.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.blankj.utilcode.util.ToastUtils;
import com.hitqz.disinfectionrobot.constant.Constants;
import com.hitqz.disinfectionrobot.databinding.DialogServerUrlBinding;

@SuppressLint("CheckResult")
public class ServerUrlDialog extends BaseDialogFragment {

    public static final String TAG = ServerUrlDialog.class.getSimpleName();

    private DialogServerUrlBinding mBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, androidx.appcompat.R.style.Theme_AppCompat_Dialog_Alert);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        mBinding = DialogServerUrlBinding.inflate(LayoutInflater.from(getContext()));
        putBefore();
        builder.setTitle("请输入服务器地址")
                .setView(mBinding.getRoot())
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ToastUtils.showLong("请手动杀死进程，重新启动App");
                        saveCurrent();
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
                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.GRAY);
            }
        });
        return alertDialog;
    }

    private void putBefore() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        String serverHost = pref.getString(Constants.KEY_SERVER_HOST, Constants.DEFAULT_SERVER_HOST);
        mBinding.etInput.setText(serverHost);
    }

    private void saveCurrent() {
        String serverHost = mBinding.etInput.getText().toString();
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
        editor.putString(Constants.KEY_SERVER_HOST, serverHost);
        editor.apply();
    }
}
