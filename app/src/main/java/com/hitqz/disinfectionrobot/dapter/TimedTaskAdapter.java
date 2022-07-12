package com.hitqz.disinfectionrobot.dapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hitqz.disinfectionrobot.R;

import java.util.List;

/**
 * 记录列表
 */
public class TimedTaskAdapter extends BaseAdapter {

    private List<Object> mData;
    private Context mContext;
    private int mSelectedPos = -1;

    public TimedTaskAdapter(Context mContext, List<Object> mData) {
        this.mData = mData;
        this.mContext = mContext;
    }

    public void setSelectedPos(int selectedPos) {
        mSelectedPos = selectedPos;
        notifyDataSetChanged();
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
        TextView tv = convertView.findViewById(R.id.tv_fileName);
        ViewGroup vp = convertView.findViewById(R.id.rl_background);

//        tv.setText((position + 1) + ": " + mData.get(position).getRobotName());
        if (position == mSelectedPos) {
            vp.setBackgroundColor(Color.BLUE);
        } else {
            vp.setBackgroundColor(Color.parseColor("#145EBE"));
        }
        return convertView;
    }
}
