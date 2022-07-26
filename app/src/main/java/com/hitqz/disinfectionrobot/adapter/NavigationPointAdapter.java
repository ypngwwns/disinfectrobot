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
    private static final String TAG = "NavigationPointAdapter";

    private View.OnClickListener deleteClickListener;
    private View.OnClickListener editClickListener;
    private View.OnClickListener ponitNameClickListener;
    private View.OnClickListener movetoClickListener;

    private int selectedPos = -1;

    private boolean dragable = false;

    public NavigationPointAdapter(Context context, List<NavigationPoint> data) {
        super(context, data);
    }

    public int getSelectedPos() {
        return selectedPos;
    }

    public void setSelectedPos(int selectedPos) {
        this.selectedPos = selectedPos;
    }

    public NavigationPoint getSelectdItem() {
        if (selectedPos != -1) {
            return data.get(selectedPos);
        } else {
            return null;
        }
    }

    public void setDeleteClickListener(View.OnClickListener deleteClickListener) {
        this.deleteClickListener = deleteClickListener;
    }

    public void setEditClickListener(View.OnClickListener editClickListener) {
        this.editClickListener = editClickListener;
    }

    public void setPonitNameClickListener(View.OnClickListener ponitNameClickListener) {
        this.ponitNameClickListener = ponitNameClickListener;
    }

    public void setMovetoClickListener(View.OnClickListener movetoClickListener) {
        this.movetoClickListener = movetoClickListener;
    }

    public void addList(List<NavigationPoint> data) {
        this.data = data;
    }

    public void add(NavigationPoint p) {
        if (data != null) {
            data.add(p);
        }
    }

    public boolean contain(String pointName) {
        for (NavigationPoint point :
                data) {
            if (point.name.contains(pointName)) {
                return true;
            }
        }
        return false;
    }

    public void remove(NavigationPoint navigationPoint) {
        data.remove(navigationPoint);
    }

    public void remove(int pos) {
        if (data != null && pos != -1) {
            Object selectedItem = null;
            if (pos == selectedPos) {
                selectedPos = -1;
                data.remove(pos);
            } else {
                if (selectedPos != -1) {
                    selectedItem = data.get(selectedPos);
                }

                data.remove(pos);

                if (selectedPos != -1) {
                    for (int i = 0; i < data.size(); i++) {
                        if (selectedItem == data.get(i)) {
                            selectedPos = i;
                            return;
                        }
                    }
                }
            }
        }
    }

    private void freshSelectedPointIndex() {
        Object selectedItem = data.get(selectedPos);
    }

    public void clear() {
        if (data != null) {
            data.clear();
        }
    }

    public void setDragType(boolean dragable) {
        this.dragable = dragable;
        if (dragable) {
            selectedPos = -1;
        }
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
            holder.editPoint = convertView.findViewById(R.id.edit_point);
            holder.deletePoint = convertView.findViewById(R.id.delete_point);
            holder.movetoPoint = convertView.findViewById(R.id.moveto_point);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (dragable) {
            holder.dragView.setVisibility(View.VISIBLE);
            holder.editPoint.setVisibility(View.VISIBLE);
            holder.deletePoint.setVisibility(View.VISIBLE);
            holder.movetoPoint.setVisibility(View.GONE);
            holder.pointName.setClickable(false);
        } else {
            holder.dragView.setVisibility(View.INVISIBLE);
            holder.editPoint.setVisibility(View.GONE);
            holder.deletePoint.setVisibility(View.INVISIBLE);
            holder.movetoPoint.setVisibility(View.VISIBLE);
            holder.pointName.setClickable(true);
        }
        holder.pointName.setText(data.get(position).name);

        if (deleteClickListener != null) {
            holder.deletePoint.setOnClickListener(deleteClickListener);
            holder.deletePoint.setTag(position);
        }

        if (editClickListener != null) {
            holder.editPoint.setOnClickListener(editClickListener);
            holder.editPoint.setTag(position);
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

        if (movetoClickListener != null) {
            holder.movetoPoint.setOnClickListener(movetoClickListener);
            holder.movetoPoint.setTag(position);
        }

        if (selectedPos == position) {
            holder.pointName.setSelected(true);
        } else {
            holder.pointName.setSelected(false);
        }

        return convertView;
    }

    private class ViewHolder {
        public ImageView dragView;
        public TextView pointName;
        public Button editPoint;
        public Button deletePoint;
        public Button movetoPoint;
    }
}
