package com.hitqz.disinfectionrobot.adapter;

import android.content.Context;
import android.widget.BaseAdapter;

import com.hitqz.disinfectionrobot.data.NavigationPoint;

import java.util.List;

public abstract class DragAdapter extends BaseAdapter {

    protected Context context;
    protected List<NavigationPoint> data;

    public DragAdapter(Context context, List<NavigationPoint> data) {
        this.context = context;
        this.data = data;
    }

    /**
     * 交换位置
     *
     * @param start
     * @param end
     */
    public void change(int start, int end) {
        NavigationPoint srcData = data.get(start);
        data.remove(srcData);
        data.add(end, srcData);
        notifyDataSetChanged();
    }

    public List<NavigationPoint> getDataList() {
        return data;
    }
}
