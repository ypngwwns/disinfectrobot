package com.hitqz.disinfectionrobot.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.hitqz.disinfectionrobot.R;
import com.hitqz.disinfectionrobot.data.MapArea;

import java.util.List;

@SuppressLint("CheckResult")
public class SelectDisinfectAreaAdapter extends BaseAdapter {
    public static final String TAG = SelectDisinfectAreaAdapter.class.getSimpleName();

    private final List<MapArea> mList;
    private int mSelectedPos = -1;
    private RadioButton mSelectedRadioButton = null;

    public SelectDisinfectAreaAdapter(List<MapArea> list) {
        this.mList = list;
    }

    public int getSelectedPos() {
        return mSelectedPos;
    }

    public void setSelectedPos(int selectedPos) {
        mSelectedPos = selectedPos;
        notifyDataSetChanged();
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
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select_disinfect_area, parent, false);
        TextView tv1 = convertView.findViewById(R.id.tv1);
        RadioButton rb1 = convertView.findViewById(R.id.rb_selected);
        String name = mList.get(position).areaName;
        tv1.setText(name);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position != mSelectedPos) {
                    rb1.setChecked(true);
                    mSelectedPos = position;
                    if (mSelectedRadioButton != null) {
                        mSelectedRadioButton.setChecked(false);
                    }
                    mSelectedRadioButton = rb1;
                }
            }
        });

        if (position == mSelectedPos) {
            if (mSelectedRadioButton != null) {
                mSelectedRadioButton.setChecked(false);
            }
            rb1.setChecked(true);
            mSelectedRadioButton = rb1;
        } else {
            rb1.setChecked(false);
        }
        return convertView;
    }
}
