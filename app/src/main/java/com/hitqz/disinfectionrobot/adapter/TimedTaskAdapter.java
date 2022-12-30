package com.hitqz.disinfectionrobot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;

import com.hitqz.disinfectionrobot.R;
import com.hitqz.disinfectionrobot.data.Task;

import java.util.List;

/**
 * 记录列表
 */
public class TimedTaskAdapter extends BaseAdapter {

    private final List<Task> mData;
    private final Context mContext;
    private View.OnClickListener mOnClickListener;
    private IOnCheckChangeListener mIOnCheckChangeListener;

    public TimedTaskAdapter(Context mContext, List<Task> mData) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_record, parent, false);
        SwitchCompat switchCompat = convertView.findViewById(R.id.sc_task);
        TextView time = convertView.findViewById(R.id.tv_time);
        TextView tv1 = convertView.findViewById(R.id.tv_1);
        switchCompat.setTag(position);
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mIOnCheckChangeListener != null) {
                    mIOnCheckChangeListener.onCheckChange(position, isChecked);
                }
            }
        });
        time.setText(mData.get(position).jobTime);
        tv1.setText(mData.get(position).areaName);
        switchCompat.setChecked(mData.get(position).jobStatus == 0);
        convertView.setTag(position);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickListener != null) {
                    mOnClickListener.onClick(v);
                }
            }
        });
        return convertView;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public void setOnCheckChangeListener(IOnCheckChangeListener listener) {
        mIOnCheckChangeListener = listener;
    }

    public interface IOnCheckChangeListener {
        void onCheckChange(int position, boolean check);
    }
}
