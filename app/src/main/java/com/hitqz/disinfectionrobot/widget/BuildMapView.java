package com.hitqz.disinfectionrobot.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import androidx.annotation.Nullable;

import com.hitqz.disinfectionrobot.R;
import com.hitqz.disinfectionrobot.data.BuildMap;
import com.hitqz.disinfectionrobot.data.OccupancyGrid;
import com.hitqz.disinfectionrobot.data.Pose;
import com.hitqz.disinfectionrobot.util.AngleUtil;

public class BuildMapView extends View {
    private static final String TAG = BuildMapView.class.getSimpleName();

    private static final int POINT_SIZE = 8;
    private final Matrix mDragScaleMatrix = new Matrix();
    public Pose mRobotoPos;
    private int mViewWidth;
    private int mViewHeight;
    private Paint mRobotPaint;
    private Paint mBorderPaint;
    private volatile boolean mIsBuildNow = false;
    private BuildMap mBuildMap = new BuildMap();
    /**
     * 以下用于建图缩放的动画
     */
    private boolean mIsScale;
    private int mDownX;
    private int mDownY;
    private int mLastX;
    private int mLastY;
    private boolean mPointer;
    private GestureDetector mGestureDetector;
    private ScaleGestureDetector mScaleGestureDetector;

    public BuildMapView(Context context) {
        this(context, null);
    }

    public BuildMapView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BuildMapView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        InitPaint();
        InitGesture();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHeight = h;

        mBuildMap.setImageShowOrigin(w, h);

        // 这两个操作，目的是解决建图时显示的地图和实际的地图出现镜像翻转的问题，首先对显示地图进行左右翻转，再对地图进行左右翻转
        mDragScaleMatrix.postRotate(-90, getWidth() / 2f, getHeight() / 2f);
        mDragScaleMatrix.postScale(-1, 1, getWidth() / 2f, getHeight() / 2f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.concat(mDragScaleMatrix);
        DrawMap(canvas);
        DrawRobotPoint(canvas);
        canvas.restore();
    }

    private void InitPaint() {
        mRobotPaint = new Paint();             // 创建画笔
        mRobotPaint.setColor(getResources().getColor(R.color.colorAccent));           // 画笔颜色 - 黑色
        mRobotPaint.setStyle(Paint.Style.FILL);    // 填充模式 - 填充
        mRobotPaint.setStrokeWidth(10);            // 边框宽度 - 10
        mBorderPaint = new Paint();
        mBorderPaint.setColor(getResources().getColor(R.color.colorPrimary));
        mBorderPaint.setStyle(Paint.Style.STROKE);
    }

    private void InitGesture() {
        mGestureDetector = new GestureDetector(getContext(), new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }
        });
        mScaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleGestureDetector.OnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                float scaleFactor = detector.getScaleFactor();

                mDragScaleMatrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());
                invalidate();

                return true;
            }

            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                mIsScale = true;
                return true;
            }

            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {
                mIsScale = false;
            }
        });
    }

    // 绘制地图
    private void DrawMap(Canvas canvas) {
        if (isBuildNow()) {
            if (mBuildMap.getBitMap() != null) {
                canvas.save();
                // debug用，原点坐标
//                canvas.drawText("绘制起点坐标：" + mBuildMap.getImageShowOrigin().x + " " + mBuildMap.getImageShowOrigin().y
//                                + " 图片宽高：" + mBuildMap.getBitMap().getWidth() + " " + mBuildMap.getBitMap().getHeight() +
//                                " View宽高：" + mViewWidth + " " + mViewHeigth, 50, 50, mBorderPaint);
                // 确定图片的显示坐标位置
                canvas.drawLine(mBuildMap.getImageShowOrigin().x,
                        mBuildMap.getImageShowOrigin().y,
                        mBuildMap.getImageShowOrigin().x,
                        mBuildMap.getImageShowOrigin().y + mBuildMap.getmMapheight(), mBorderPaint);
                canvas.drawBitmap(mBuildMap.getBitMap(), mBuildMap.getImageShowOrigin().x, mBuildMap.getImageShowOrigin().y, mRobotPaint);
                canvas.restore();
            }
        }
    }

    public void setMapData(OccupancyGrid map) {
        mBuildMap.reloadMap(map);
        mBuildMap.setImageShowOrigin(mViewWidth, mViewHeight);
    }

    public void setRobotPos(Pose pose) {
        mRobotoPos = pose;
    }

    // 绘制机器人所在点
    private void DrawRobotPoint(Canvas canvas) {
        if (isBuildNow()) {
            if (mRobotoPos == null) {
                return;
            }
            final int size = (int) (POINT_SIZE * mBuildMap.getShowScale());

            canvas.save();
//            Log.i(TAG, "DrawRobotPoint: =====" + mRobotoPos.getPointF().toString());
            PointF drawPoint = mBuildMap.getDrawPoint(new PointF((float) mRobotoPos.getPosition().getX(), (float) mRobotoPos.getPosition().getY()));
            canvas.translate(drawPoint.x, drawPoint.y);
            canvas.rotate((float) AngleUtil.radian2Angle(mRobotoPos.getOrientation().getYaw()));
//            canvas.rotate((float) (mRobotoPos.getOrientation().getYaw() * 180 / Math.PI));

            Path path = new Path();
            path.moveTo(size, 0);
            path.lineTo(-size, -size);
            path.lineTo(-size / 2f, 0);
            path.lineTo(-size, size);
            path.lineTo(size, 0);

            canvas.drawPath(path, mRobotPaint);
            canvas.restore();
        }
    }

    public boolean isBuildNow() {
        return mIsBuildNow;
    }

    public void setBuildNow(boolean buildNow) {
        mIsBuildNow = buildNow;
    }

    public void reset() {
        mBuildMap = new BuildMap();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        mGestureDetector.onTouchEvent(event);
        mScaleGestureDetector.onTouchEvent(event);

        if (event.getPointerCount() > 1) {
            mPointer = true;
        }

        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mDownX = x;
                mDownY = y;
                mPointer = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (!mIsScale) {
                    int dx = Math.abs(x - mDownX);
                    int dy = Math.abs(y - mDownY);
                    if (dx > 10 && dy > 10 && !mPointer) {
                        dx = x - mLastX;
                        dy = y - mLastY;
                        mDragScaleMatrix.postTranslate(dx, dy);
                        invalidate();
                    }
                }

                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        mLastX = x;
        mLastY = y;
        return true;
    }
}
