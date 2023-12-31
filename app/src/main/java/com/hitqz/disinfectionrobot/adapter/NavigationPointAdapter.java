package com.hitqz.disinfectionrobot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hitqz.disinfectionrobot.R;
import com.hitqz.disinfectionrobot.data.NavigationPoint;

import java.util.List;

public class NavigationPointAdapter extends DragAdapter {
    private static final String TAG = NavigationPointAdapter.class.getSimpleName();

    private View.OnClickListener deleteClickListener;
    private View.OnClickListener ponitNameClickListener;

    private boolean dragable = true;

    public NavigationPointAdapter(Context context, List<NavigationPoint> data) {
        super(context, data);
    }

    public void setDeleteClickListener(View.OnClickListener deleteClickListener) {
        this.deleteClickListener = deleteClickListener;
    }

    public void setPonitNameClickListener(View.OnClickListener ponitNameClickListener) {
        this.ponitNameClickListener = ponitNameClickListener;
    }

    public void add(NavigationPoint p) {
        if (data != null) {
            data.add(p);
        }
    }

    public boolean contain(NavigationPoint p) {
        return data.contains(p);
    }

    public void remove(NavigationPoint navigationPoint) {
        data.remove(navigationPoint);
    }

    public void remove(int pos) {
        if (data != null && pos != -1) {
            data.remove(pos);
        }
    }

    public void clear() {
        if (data != null) {
            data.clear();
        }
    }

    public void setDragType(boolean dragable) {
        this.dragable = dragable;
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position) {
        if (data == null) {
            return null;
        }
        return data.get(position);
    }

    @Override
    public int getCount() {
        if (data == null) {
            return 0;
        }
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_navigation_point, parent, false);
            holder = new ViewHolder();
            holder.dragView = convertView.findViewById(R.id.list_drag);
            holder.pointName = convertView.findViewById(R.id.point_name);
            holder.deletePoint = convertView.findViewById(R.id.delete_point);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (dragable) {
            holder.dragView.setVisibility(View.VISIBLE);
            holder.pointName.setClickable(false);
        } else {
            holder.dragView.setVisibility(View.INVISIBLE);
            holder.pointName.setClickable(true);
        }

        if ("2".equals(data.get(position).type)) {
            holder.pointName.setText("充电点");
        } else {
            holder.pointName.setText("导航点" + (position + 1));
        }

        if (deleteClickListener != null) {
            holder.deletePoint.setOnClickListener(deleteClickListener);
            holder.deletePoint.setTag(position);
        }

        if (dragable) {
            holder.pointName.setOnClickListener(null);
            holder.pointName.setTag(position);
        } else {
            if (ponitNameClickListener != null) {
                holder.pointName.setOnClickListener(ponitNameClickListener);
                holder.pointName.setTag(position);
            }
        }

        return convertView;
    }

    private class ViewHolder {
        public ImageView dragView;
        public TextView pointName;
        public Button deletePoint;
    }
}
