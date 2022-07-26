package com.hitqz.disinfectionrobot.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.hitqz.disinfectionrobot.adapter.DragAdapter;
import com.hitqz.disinfectionrobot.adapter.NavigationPointAdapter;

public class DragListView extends ListView {
    private static final String TAG = "DragListView";
    private final int TOUCH_SLOP = 20;
    private WindowManager.LayoutParams windowParams;
    private WindowManager windowManager;
    private ImageView dragImageView;
    private int offsetScreenTop; //距离屏幕顶部的位置
    private int offsetViewTop;  //手指按下位置距离item顶部的位置
    private int dragPosition;
    //上次点击按下的坐标
    private int mLastMotionX, mLastMotionY;
    private Runnable mLongPressRunnable;
    private Bitmap mCurItemBmp;

    //当前是否长按状态
    private boolean mIsLongTouch;

    private boolean dragType = false;

    public DragListView(Context context) {
        super(context);
    }

    public DragListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mLongPressRunnable = new Runnable() {
            @Override
            public void run() {
                mIsLongTouch = true;
                startDrag(mCurItemBmp, mLastMotionY);
            }
        };
    }

    //itemview里有view处理点击事件时， 当前ListView无法收到UP或CANCEL消息
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL) {
            removeCallbacks(mLongPressRunnable);
            stopDrag();
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        int rawY = (int) ev.getRawY();
        if (!dragType) {
            return super.onInterceptTouchEvent(ev);
        }
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            mLastMotionX = x;
            mLastMotionY = y;
            mIsLongTouch = false;
            stopDrag();  //复位
            removeCallbacks(mLongPressRunnable);

            int currentPostion = dragPosition = pointToPosition(x, y);

            //拖拽效果设置热区
            if (currentPostion == AdapterView.INVALID_POSITION) {
                return super.onInterceptTouchEvent(ev);
            }
            postDelayed(mLongPressRunnable, ViewConfiguration.getLongPressTimeout());

            //getChildAt是获取可见位置的item
            ViewGroup itemView = (ViewGroup) getChildAt(currentPostion - getFirstVisiblePosition());
            offsetScreenTop = rawY - y;
            offsetViewTop = y - itemView.getTop();

            itemView.setDrawingCacheEnabled(true);// 开启cache.
            mCurItemBmp = Bitmap.createBitmap(itemView.getDrawingCache());// 根据cache创建一个新的bitmap对象.
            itemView.setDrawingCacheEnabled(false);// 一定关闭cache，否则复用会出现错乱
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int y = (int) ev.getY();
        int x = (int) ev.getX();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(mLastMotionX - x) > TOUCH_SLOP || Math.abs(mLastMotionY - y) > TOUCH_SLOP) {
                    //滑动超过阈值时判定不是长按
                    removeCallbacks(mLongPressRunnable);
                }

                if (mIsLongTouch) {
                    onDrag(y);
                    getChildAt(dragPosition - getFirstVisiblePosition()).setVisibility(View.INVISIBLE);
                    return true; //避免列表滑动
                }
                removeCallbacks(mLongPressRunnable);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                removeCallbacks(mLongPressRunnable);
                if (mIsLongTouch) {
                    stopDrag();
                    getChildAt(dragPosition - getFirstVisiblePosition()).setVisibility(View.VISIBLE);

                    mIsLongTouch = false;
                }
                break;
        }

        return super.onTouchEvent(ev);
    }

    private void startDrag(Bitmap bm, int y) {
        int[] location = new int[2];
        getLocationInWindow(location);

        /***
         * 初始化window.
         */
        windowParams = new WindowManager.LayoutParams();
        windowParams.gravity = Gravity.TOP;
        windowParams.x = 0;
        windowParams.y = y - offsetViewTop + offsetScreenTop;
        windowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        windowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        windowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE// 不需获取焦点
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE// 不需接受触摸事件
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON// 保持设备常开，并保持亮度不变。
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;// 窗口占满整个屏幕，忽略周围的装饰边框（例如状态栏）。此窗口需考虑到装饰边框的内容。

//        windowParams.format = PixelFormat.TRANSLUCENT;// 默认为不透明，这里设成透明效果.
        windowParams.windowAnimations = 0;// 窗口所使用的动画设置

        ImageView imageView = new ImageView(getContext());
        imageView.setImageBitmap(bm);
        windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.addView(imageView, windowParams);
        dragImageView = imageView;
    }

    /**
     * 拖动图像在ListView范围内， 不能超过边界
     *
     * @param y
     */
    private void onDrag(int y) {
        int offsetTop = y - offsetViewTop; //顶部不能出界
        int offsetBottom = offsetViewTop + getHeight() - dragImageView.getHeight();  //下边界
        if (dragImageView != null
                && offsetTop >= 0
                && offsetTop <= getChildAt(getChildCount() - 1).getTop()
                && y <= offsetBottom) {
            windowParams.alpha = 0.8f;// 透明度
            windowParams.y = y - offsetViewTop + offsetScreenTop;// 移动y值.//记得要加上dragOffset，windowManager计算的是整个屏幕.(标题栏和状态栏都要算上)
            windowManager.updateViewLayout(dragImageView, windowParams);// 时时移动.
        }

        onChange(y);

        scrollListView(y);
    }

    /**
     * 同步滑动ListView
     *
     * @param y
     */
    private void scrollListView(int y) {
        View view = getChildAt(dragPosition - getFirstVisiblePosition());
        int offsetY = mLastMotionY - y;

        if (y < getHeight() / 3 && y < mLastMotionY) { //listview向上滑
            setSelectionFromTop(dragPosition, offsetY + view.getTop());
        } else if (y > getHeight() / 3 * 2 && y > mLastMotionY) { //listview向下滑
            setSelectionFromTop(dragPosition, offsetY + view.getTop());
        }
        mLastMotionY = y;
    }

    /**
     * 同步改变item的位置
     *
     * @param y
     */
    private void onChange(int y) {
        int currentPostion = pointToPosition(0, y);

        if (currentPostion == AdapterView.INVALID_POSITION) {
            currentPostion = dragPosition;
        }

        if (dragPosition != currentPostion) {
            DragAdapter adapter = (DragAdapter) getAdapter();
            adapter.change(dragPosition, currentPostion);
            switchChild(dragPosition, currentPostion);
        }

        dragPosition = currentPostion;
    }

    /***
     * 切换隐藏的位置
     */
    private void switchChild(int start, int end) {
        getChildAt(start - getFirstVisiblePosition()).setVisibility(View.VISIBLE);
        getChildAt(end - getFirstVisiblePosition()).setVisibility(View.INVISIBLE);
    }

    /**
     * 停止拖动，删除影像
     */
    public void stopDrag() {
        if (dragImageView != null) {
            windowManager.removeView(dragImageView);
            dragImageView = null;
        }
    }

    public void setDragType(boolean dragType) {
        this.dragType = dragType;
        if (getAdapter() instanceof NavigationPointAdapter) {
            ((NavigationPointAdapter) getAdapter()).setDragType(dragType);
        }
    }
}
