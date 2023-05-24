package com.hitqz.disinfectionrobot.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.hitqz.disinfectionrobot.R;
import com.hitqz.disinfectionrobot.adapter.MapListAdapter;
import com.hitqz.disinfectionrobot.databinding.DialogMapListBinding;

import java.util.List;

@SuppressLint("CheckResult")
public class MapListDialog extends BaseDialogFragment {

    public static final String TAG = MapListDialog.class.getSimpleName();

    private DialogMapListBinding mBinding;
    private OnClickListener mOnClickListener;
    private List<String> mList;
    private int mSelectPosition = -1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.Theme_AppCompat_Dialog_Alert);
    }

    public void setList(List<String> list) {
        mList = list;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        mBinding = DialogMapListBinding.inflate(LayoutInflater.from(getContext()));
        MapListAdapter mapListAdapter = new MapListAdapter();
        mapListAdapter.setList(mList);
        mBinding.lvMapList.setAdapter(mapListAdapter);
        mBinding.lvMapList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mSelectPosition == position) {//防止重复点击触发刷新
                    return;
                }
                mSelectPosition = position;
                mapListAdapter.setSelectedPos(position);
            }
        });
        builder.setTitle("地图列表")
                .setView(mBinding.getRoot())
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (mOnClickListener != null && mSelectPosition != -1) {
                            mOnClickListener.onConfirm(mList.get(mSelectPosition));
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
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
            }
        });

        // Create the AlertDialog object and return it
        return alertDialog;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onConfirm(String text);
    }
}
