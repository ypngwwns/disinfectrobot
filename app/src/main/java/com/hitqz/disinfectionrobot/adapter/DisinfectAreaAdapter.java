package com.hitqz.disinfectionrobot.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hitqz.disinfectionrobot.R;
import com.hitqz.disinfectionrobot.data.Area;

import java.util.List;

@SuppressLint("CheckResult")
public class DisinfectAreaAdapter extends BaseAdapter {
    public static final String TAG = DisinfectAreaAdapter.class.getSimpleName();

    private final List<Area> mList;
    private View.OnClickListener mOnClickListener;

    public DisinfectAreaAdapter(List<Area> list) {
        this.mList = list;
    }

    @Override
    public int getCount() {
        if (mList == null) {
            return 0;
        }
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        if (mList == null) {
            return null;
        }
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_disinfect_area, parent, false);
        TextView tv1 = convertView.findViewById(R.id.tv1);
        tv1.setText(mList.get(position).areaName);
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
}
