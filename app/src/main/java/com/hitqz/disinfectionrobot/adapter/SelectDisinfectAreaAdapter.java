package com.hitqz.disinfectionrobot.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hitqz.disinfectionrobot.R;

import java.util.List;

@SuppressLint("CheckResult")
public class SelectDisinfectAreaAdapter extends BaseAdapter {
    public static final String TAG = SelectDisinfectAreaAdapter.class.getSimpleName();

    private final List<String> mList;

    public SelectDisinfectAreaAdapter(List<String> list) {
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select_disinfect_area, parent, false);
            holder = new ViewHolder();
            holder.tv1 = convertView.findViewById(R.id.tv1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String name = mList.get(position);
        holder.tv1.setText(name);
        return convertView;
    }

    public class ViewHolder {
        private TextView tv1;
    }
}
