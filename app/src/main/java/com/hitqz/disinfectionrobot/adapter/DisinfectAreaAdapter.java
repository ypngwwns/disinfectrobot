package com.hitqz.disinfectionrobot.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hitqz.disinfectionrobot.R;

import java.util.List;

@SuppressLint("CheckResult")
public class DisinfectAreaAdapter extends BaseAdapter {
    public static final String TAG = DisinfectAreaAdapter.class.getSimpleName();

    private final List<String> mList;
    private int mSelectedPos = -1;

    public DisinfectAreaAdapter(List<String> list) {
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
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_disinfect_area, parent, false);
            holder = new ViewHolder();
            holder.tv1 = convertView.findViewById(R.id.tv1);
            holder.vp = convertView.findViewById(R.id.vp_background);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String name = mList.get(position);
        holder.tv1.setText(name);
        if (position == mSelectedPos) {
            holder.vp.setBackgroundColor(Color.BLUE);
        } else {
            holder.vp.setBackgroundColor(Color.parseColor("#03a9f4"));
        }
        return convertView;
    }

    public void setSelectedPos(int selectedPos) {
        mSelectedPos = selectedPos;
        notifyDataSetChanged();
    }

    public class ViewHolder {
        private TextView tv1;
        private ViewGroup vp;
    }
}
