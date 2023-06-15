package com.hitqz.disinfectionrobot.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;

import com.hitqz.disinfectionrobot.R;
import com.hitqz.disinfectionrobot.data.NavigationPoint;

import java.util.List;

public class DisinfectPointAdapter extends DragAdapter {
    private static final String TAG = DisinfectPointAdapter.class.getSimpleName();

    private View.OnClickListener deleteClickListener;
    private View.OnClickListener ponitNameClickListener;

    private boolean dragable = true;

    public DisinfectPointAdapter(Context context, List<NavigationPoint> data) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_disinfect_point, parent, false);
            holder = new ViewHolder();
            holder.dragView = convertView.findViewById(R.id.list_drag);
            holder.pointName = convertView.findViewById(R.id.point_name);
            holder.deletePoint = convertView.findViewById(R.id.delete_point);
            holder.switchCompat = convertView.findViewById(R.id.sc_disinfect);
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
        holder.pointName.setText("清洁点" + (position + 1));
        holder.switchCompat.setTag(position);
        holder.switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int index = (int) buttonView.getTag();
                Log.d(TAG, "index===" + index);
                if (isChecked) {
                    data.get(index).action = 1;
                } else {
                    data.get(index).action = 0;
                }
                notifyDataSetChanged();
            }
        });
        //必须设置在监听器后面
        holder.switchCompat.setChecked(data.get(position).action == 1);

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
        public ImageView deletePoint;
        public SwitchCompat switchCompat;
    }
}
