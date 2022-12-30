package com.hitqz.disinfectionrobot.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.BaseAdapter;

import androidx.annotation.Nullable;
import androidx.core.math.MathUtils;

import com.hitqz.disinfectionrobot.R;
import com.hitqz.disinfectionrobot.data.LaserScan;
import com.hitqz.disinfectionrobot.data.NavigationPoint;
import com.hitqz.disinfectionrobot.util.AngleUtil;

import java.util.ArrayList;
import java.util.List;

public class NavigationView extends View {
    public static final String TAG = NavigationView.class.getSimpleName();

    public static final float MAX_SCALE_FACTER = 5f;
    public static final float MIN_SCALE_FACTER = 0.5f;
    public static final float DEFAULT_TEXT_SIZE = 70f;
    private final PointF mStart = new PointF();
    private final Matrix mBitmapMatrix = new Matrix();
    private Paint mTextPaint;
    private Paint mLinePaint;
    private Paint mPointPaint;
    private Paint mRobotPaint;
    private Paint mSelectedPaint;
    private Paint laserLinePaint;
    private Paint laserPointPaint;
    private Bitmap rechargeBitmap;
    private Bitmap selectedPointBitmap;
    private Bitmap naviPointBitmap;
    // 机器人坐标
    private NavigationPoint mRobotPos;
    // 充电点
    private List<NavigationPoint> mRechargePos;
    private List<NavigationPoint> mNavigationPoints;
    private List<NavigationPoint> mSelectedNavigationPoints = new ArrayList<>();
    private boolean mShowLaserScan = true;
    // 地图信息
    private float mResolution = 0f;
    private float mOriginX;
    private float mOriginY;
    private Bitmap mBitmap;
    // 激光数据
    private LaserScan mLaserScan;
    // 移动缩放缓存
    private int mTouchMode = 0;//1：缩放，2：移动
    private Matrix mMatrix;
    private float mOriDis = 0f;// 初始的两个手指按下的触摸点的距离
    private float mScaleSum = 1f;
    private PointF mMid = new PointF();
    private GestureDetector mGestureDetector;
    private OnLongPressListener mOnLongPressListener;
    private BaseAdapter mBaseAdapter;

    public NavigationView(Context context) {
        super(context);
        init();
    }

    public NavigationView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NavigationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 取两指的中心点坐标
     */
    public static PointF getMid(MotionEvent event) {
        float midX = (event.getX(1) + event.getX(0)) / 2;
        float midY = (event.getY(1) + event.getY(0)) / 2;
        return new PointF(midX, midY);
    }

    private void init() {
        mTextPaint = new Paint();             // 创建画笔
        mTextPaint.setColor(getResources().getColor(R.color.black));           // 画笔颜色 - 黑色
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setStrokeWidth(1);

        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeWidth(5f);
        mLinePaint.setColor(Color.parseColor("#F6BD16"));

        mRobotPaint = new Paint();             // 创建画笔
        mRobotPaint.setColor(getResources().getColor(R.color.colorAccent));           // 画笔颜色 - 黑色
        mRobotPaint.setStyle(Paint.Style.FILL);    // 填充模式 - 填充
        mRobotPaint.setStrokeWidth(10);            // 边框宽度 - 10

        mPointPaint = new Paint();             // 创建画笔
        mPointPaint.setColor(getResources().getColor(R.color.colorPrimaryDark));           // 画笔颜色 - 黑色
        mPointPaint.setStyle(Paint.Style.STROKE);

        mSelectedPaint = new Paint();             // 创建画笔
        mSelectedPaint.setColor(getResources().getColor(R.color.colorAccent));           // 画笔颜色 - 黑色
        mSelectedPaint.setStyle(Paint.Style.STROKE);

        laserLinePaint = new Paint();             // 创建画笔
        laserLinePaint.setColor(getResources().getColor(R.color.laserLineColor));           // 画笔颜色 - 黑色
        laserLinePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        laserLinePaint.setStrokeWidth(2);
        laserLinePaint.setAlpha(100);

        laserPointPaint = new Paint();             // 创建画笔
        laserPointPaint.setColor(getResources().getColor(R.color.laserPointColor));           // 画笔颜色 - 黑色
        laserPointPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        laserPointPaint.setStrokeWidth(2);
        laserPointPaint.setAlpha(100);

        rechargeBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.navi_recharge);

        naviPointBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.navi_point);

        selectedPointBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.select_point);

        Log.d(TAG, "naviPointBitmap.getWidth() / 2f:" + naviPointBitmap.getWidth() / 2f);
    }

    public void setSelectable(boolean selectable) {
        if (selectable) {
            mGestureDetector = new GestureDetector(getContext(), new MapViewGestureListener(this));
        } else {
            mGestureDetector = null;
        }
    }

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
        if (mBitmap != null) {
            int viewWidth = getWidth();
            int viewHeight = getHeight();
            int rawWidth = mBitmap.getWidth();
            int rawHeight = mBitmap.getHeight();

            Log.d(TAG, "mBitmap width ===" + rawWidth + "mBitmap height ===" + rawHeight);
            float heightScale = ((float) viewWidth) / rawWidth;
            float widthScale = ((float) viewHeight) / rawHeight;
            float scale = Math.min(widthScale, heightScale);
            mMatrix = new Matrix();
            float anchorX = (viewWidth - rawWidth * scale) / 2f;
            float anchorY = (viewHeight - rawHeight * scale) / 2f;
            mMatrix.postScale(scale, scale);
            mMatrix.postTranslate(anchorX, anchorY);
        }
        postInvalidate();
    }

    private void drawMap(Canvas canvas) {
        if (mBitmap != null) {
            canvas.save();
            canvas.setMatrix(mMatrix);
            canvas.drawBitmap(mBitmap, 0, 0, null);
            canvas.restore();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 绘制有先后顺序
        //1.绘制地图
        drawMap(canvas);
        // 绘制底盘返回的数据需要两个步骤：
        // 1) 数据坐标需根据resolution和origin计算坐标在原始地图（pgm生成的Bitmap）上的坐标
        // 2) 根据当前移动和缩放绘制
        //2.绘制激光
        drawLaserScan(canvas);
        //3.绘制机器人位置
        drawRobotPosition(canvas);
        //4.绘制消毒路径
        drawPath(canvas);
        //5.绘制导航点位
        drawNavPosition(canvas);
        //6.绘制充电点
        drawRechargePosition(canvas);
    }

    private void drawPath(Canvas canvas) {
        if (mSelectedNavigationPoints != null && mSelectedNavigationPoints.size() > 0) {
            for (int i = 0; i < mSelectedNavigationPoints.size(); i++) {
                if (i > 0) {
                    NavigationPoint drawPoint = mSelectedNavigationPoints.get(i);
                    NavigationPoint lastPoint = mSelectedNavigationPoints.get(i - 1);
                    canvas.save();
                    canvas.setMatrix(mMatrix);
//                    canvas.translate(drawPoint.drawX, drawPoint.drawY);
                    canvas.drawLine(lastPoint.drawX, lastPoint.drawY, drawPoint.drawX, drawPoint.drawY, mLinePaint);
                    //绘制箭头
                    drawTriangle(canvas, mLinePaint, lastPoint.drawX, lastPoint.drawY, drawPoint.drawX, drawPoint.drawY,
                            40, 20);
                    canvas.restore();
                }
            }
        }
    }

    private void drawRechargePosition(Canvas canvas) {
        if (mBitmap != null && mRechargePos != null) {
            for (int i = 0; i < mRechargePos.size(); i++) {
                NavigationPoint rechargePos = mRechargePos.get(i);
                canvas.save();
                canvas.setMatrix(mMatrix);
                NavigationPoint drawPoint = getDrawPoint(rechargePos);
                canvas.translate(drawPoint.drawX, drawPoint.drawY);
                // 绘制图形时角度要取负数
                // 安卓是以顺时针为坐标系
                canvas.rotate(-drawPoint.angle);
                mBitmapMatrix.reset();
                mBitmapMatrix.setTranslate(-rechargeBitmap.getWidth() / 2f, -rechargeBitmap.getHeight() / 2f);
                mBitmapMatrix.postScale(2f / mScaleSum, 2f / mScaleSum);
                canvas.drawBitmap(rechargeBitmap, mBitmapMatrix, null);
                canvas.rotate(drawPoint.angle);
                canvas.drawText(String.valueOf(i + 1), 0, 20, mTextPaint);
                canvas.restore();
            }
        }
    }

    private void drawNavPosition(Canvas canvas) {
        if (mNavigationPoints != null && mBitmap != null && mNavigationPoints.size() > 0) {
            List<NavigationPoint> navigationPoints = mNavigationPoints;
            mTextPaint.setTextSize(DEFAULT_TEXT_SIZE / mScaleSum);
            for (int i = 0; i < navigationPoints.size(); i++) {
                NavigationPoint navigationPoint = navigationPoints.get(i);
                canvas.save();
                canvas.setMatrix(mMatrix);
                NavigationPoint drawPoint = getDrawPoint(navigationPoint);

                canvas.translate(drawPoint.drawX, drawPoint.drawY);
                Bitmap bitmap = null;
                if (mSelectedNavigationPoints.contains(drawPoint)) {
//                    String value = String.valueOf(mSelectedNavigationPoints.indexOf(navigationPoint) + 1);
//                    canvas.drawText(value, -15, -50, mTextPaint);
                    bitmap = selectedPointBitmap;
                } else {
                    bitmap = naviPointBitmap;
                }

                // 绘制图形时角度要取负数
                // 安卓是以顺时针为坐标系
                canvas.rotate(-drawPoint.angle);
                mBitmapMatrix.reset();
                mBitmapMatrix.setTranslate(-bitmap.getWidth() / 2f, -bitmap.getHeight() / 2f);
                mBitmapMatrix.postScale(2f / mScaleSum, 2f / mScaleSum);
                canvas.drawBitmap(bitmap, mBitmapMatrix, null);
                canvas.rotate(drawPoint.angle);
                canvas.drawText(String.valueOf(i + 1), 0, 20, mTextPaint);
                canvas.restore();
            }
        }
    }

    /**
     * 绘制三角
     *
     * @param canvas
     * @param fromX
     * @param fromY
     * @param toX
     * @param toY
     * @param height
     * @param bottom
     */
    private void drawTriangle(Canvas canvas, Paint paintLine, float fromX, float fromY, float toX, float toY, int height, int bottom) {
        try {
            float juli = (float) Math.sqrt((toX - fromX) * (toX - fromX)
                    + (toY - fromY) * (toY - fromY));// 获取线段距离
            float juliX = toX - fromX;// 有正负，不要取绝对值
            float juliY = toY - fromY;// 有正负，不要取绝对值
            float dianX = toX - (height / juli * juliX);
            float dianY = toY - (height / juli * juliY);
            float dian2X = fromX + (height / juli * juliX);
            float dian2Y = fromY + (height / juli * juliY);
            //终点的箭头
            Path path = new Path();
            path.moveTo(toX, toY);// 此点为三边形的起点
            path.lineTo(dianX + (bottom / juli * juliY), dianY
                    - (bottom / juli * juliX));
            path.lineTo(dianX - (bottom / juli * juliY), dianY
                    + (bottom / juli * juliX));
            path.close(); // 使这些点构成封闭的三边形
            canvas.drawPath(path, paintLine);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void drawLaserScan(Canvas canvas) {
        if (mShowLaserScan && mLaserScan != null && mBitmap != null && mRobotPos != null) {
            float angleMin = mLaserScan.getAngleMin();
            float rangeMin = mLaserScan.getRangeMin();
            float rangeMax = mLaserScan.getRangeMax();
            List<Float> rangeList = mLaserScan.getRangesList();

            canvas.save();
            canvas.setMatrix(mMatrix);

            float curAngle = angleMin;
            for (int i = 0; i < rangeList.size(); i++) {
                float curRange = rangeList.get(i);
                if (curRange < rangeMin || curRange > rangeMax) {
                    curAngle += mLaserScan.getAngleIncrement();
                    continue;
                }
                float robotRadius = (float) mRobotPos.radian;
                float endX = (float) (mRobotPos.rawX + curRange * Math.cos(robotRadius + curAngle));
                float endY = (float) (mRobotPos.rawY + curRange * Math.sin(robotRadius + curAngle));

                float drawX = mRobotPos.drawX;
                float drawY = mRobotPos.drawY;
                float drawEndX = getDrawX(endX);
                float drawEndY = getDrawY(endY);

                canvas.drawLine(drawX, drawY, drawEndX, drawEndY, laserLinePaint);
                canvas.drawPoint(drawEndX, drawEndY, laserPointPaint);

                curAngle += mLaserScan.getAngleIncrement();
            }

            canvas.restore();
        }
    }

    private void drawRobotPosition(Canvas canvas) {
        if (mResolution != 0 && mBitmap != null && mRobotPos != null) {
            canvas.save();
            canvas.setMatrix(mMatrix);
            canvas.translate(mRobotPos.drawX, mRobotPos.drawY);
            // 绘制图形时角度要取负数
            // 安卓是以顺时针为坐标系
            canvas.rotate(-mRobotPos.angle);

            float size = 5f;
            Path path = new Path();
            path.moveTo(size, 0);
            path.lineTo(-size, -size);
            path.lineTo(-size / 2, 0);
            path.lineTo(-size, size);
            path.lineTo(size, 0);
            canvas.drawPath(path, mRobotPaint);
            canvas.restore();
        }
    }

    public void setRobotPoint(NavigationPoint robotPos) {
        if (mBitmap != null && robotPos != null) {
            mRobotPos = robotPos;
            getDrawPoint(robotPos);
            postInvalidate();
        }
    }

    public void setNavigationPoints(List<NavigationPoint> navigationPoints) {
        this.mNavigationPoints = new ArrayList<>();
        this.mRechargePos = new ArrayList<>();
        for (NavigationPoint p : navigationPoints) {
            if (p.name.contains("充电点")) {
                p.name = "充电点" + (mRechargePos.size() + 1);
                mRechargePos.add(p);
            } else if (p.name.contains("导航点")) {
                p.name = "导航点" + (mNavigationPoints.size() + 1);
                mNavigationPoints.add(p);
            }
        }

        postInvalidate();
    }

    public void setResolutionAndOrigin(float resolution, float originX, float originY) {
        this.mResolution = resolution;
        this.mOriginX = originX;
        this.mOriginY = originY;
        postInvalidate();
    }

    public void setLaserScan(LaserScan laserScan) {
        mLaserScan = laserScan;
        postInvalidate();
    }

    private float getDrawX(double rawX) {
        return ((Double) ((rawX - mOriginX) / mResolution)).floatValue();
    }

    private float getDrawY(double rawY) {
        return ((Double) (mBitmap.getHeight() - (rawY - mOriginY) / mResolution)).floatValue();
    }

    private NavigationPoint getDrawPoint(NavigationPoint navigationPoint) {
        navigationPoint.drawX = getDrawX((float) navigationPoint.rawX);
        navigationPoint.drawY = getDrawY((float) navigationPoint.rawY);
        navigationPoint.angle = ((Double) AngleUtil.radian2Angle(navigationPoint.radian)).floatValue();

        return navigationPoint;
    }
//
//    public void setRechargePos(NavigationPoint rechargePos) {
//        mRechargePos = rechargePos;
//        postInvalidate();
//    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mBitmap == null) {
            return false;
        }

        if (mGestureDetector != null) {
            mGestureDetector.onTouchEvent(event);
        }

        switch (event.getAction() & MotionEvent.ACTION_MASK) {//多点要带 ACTION_MASK
            case MotionEvent.ACTION_DOWN: {
                mStart.set(event.getX(), event.getY());
                mTouchMode = 2;
            }
            break;
            case MotionEvent.ACTION_POINTER_DOWN: {//第二个手指按下
                mMid = getMid(event);
                //当两指间距大于10时，计算两指中心点
                mOriDis = getDistance(event);
                if (mOriDis > 10f) {
                    mTouchMode = 1;
                }
            }
            break;
            case MotionEvent.ACTION_MOVE: {
                if (mTouchMode > 0) {
                    if (mTouchMode == 1) {
                        // 两个手指滑动
                        float newDist = getDistance(event);
                        Log.i(TAG, "onTouchEvent: newDist->" + newDist);
                        if (newDist > 10f) {
                            float scale = newDist / mOriDis;
                            scale = clampScale(scale);
                            mMatrix.postScale(scale, scale, mMid.x, mMid.y);
                            mScaleSum *= scale;
                            mOriDis = newDist;
                        }
                    } else if (mTouchMode == 2) {
                        mMatrix.postTranslate(event.getX() - mStart.x, event.getY()
                                - mStart.y);
                        mStart.x = event.getX();
                        mStart.y = event.getY();
                    }
                    postInvalidate();
                }
            }
            break;
            case MotionEvent.ACTION_UP: {
                mTouchMode = 0;
                postInvalidate();
            }
            break;
            case MotionEvent.ACTION_POINTER_UP: {//第二个手指弹起
                mTouchMode = 0;
            }
            break;
        }
        return true;
    }

    /**
     * 获取两指之间的距离
     */
    private float getDistance(MotionEvent event) {
        if ((event == null) || (event.getPointerCount() < 2)) {
            return 0f;
        }
        float x = event.getX(1) - event.getX(0);
        float y = event.getY(1) - event.getY(0);
        return (float) Math.sqrt(x * x + y * y);
    }

    private float clampScale(float scale) {
        float max = MAX_SCALE_FACTER / mScaleSum;
        float min = MIN_SCALE_FACTER / mScaleSum;
        return MathUtils.clamp(scale, min, max);
    }

    public boolean isShowLaserScan() {
        return mShowLaserScan;
    }

    public void setShowLaserScan(boolean showLaserScan) {
        mShowLaserScan = showLaserScan;
        postInvalidate();
    }

    public float getScaleSum() {
        return mScaleSum;
    }

    public OnLongPressListener getOnLongPressListener() {
        return mOnLongPressListener;
    }

    public void setOnLongPressListener(OnLongPressListener onLongPressListener) {
        mOnLongPressListener = onLongPressListener;
    }

    public float[] getRawMapAxis(float[] src) {
        float[] dst = new float[2];
        Matrix matrix = new Matrix();
        mMatrix.invert(matrix);
        matrix.mapPoints(dst, src);
        dst[0] = dst[0] * mResolution + mOriginX;
        dst[1] = (mBitmap.getHeight() - dst[1]) * mResolution + mOriginY;
        return dst;
    }

    public float[] getDrawMapAxis(float[] src) {
        float[] dst = new float[2];
        Matrix matrix = new Matrix();
        mMatrix.invert(matrix);
        matrix.mapPoints(dst, src);
        return dst;
    }

    public void setPointAdapter(BaseAdapter baseAdapter) {
        mBaseAdapter = baseAdapter;
    }

    public List<NavigationPoint> getSelectedNavigationPoints() {
        return mSelectedNavigationPoints;
    }

    public void setSelectedNavigationPoints(List<NavigationPoint> selectedNavigationPoints) {
        mSelectedNavigationPoints = selectedNavigationPoints;
    }

    public interface OnLongPressListener {
        void onLongPress(float x, float y);
    }

    private static class MapViewGestureListener extends GestureDetector.SimpleOnGestureListener {

        private final NavigationView mMapView;

        public MapViewGestureListener(NavigationView mapView) {
            this.mMapView = mapView;
        }

        @Override
        public void onLongPress(MotionEvent e) {

//            if (mMapView != null && mMapView.getOnLongPressListener() != null) {
//                float x = e.getX();
//                float y = e.getY();
//                float[] mapXY = mMapView.getRawMapAxis(new float[]{x, y});
//                mMapView.getOnLongPressListener().onLongPress(mapXY[0], mapXY[1]);
//            }
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            // 判断当前位置是否有导航点
            float x = e.getX();
            float y = e.getY();
            float[] mapXY = mMapView.getDrawMapAxis(new float[]{x, y});

            float size = 50f;
            for (int i = 0; i < mMapView.mNavigationPoints.size(); i++) {
                NavigationPoint navigationPoint = mMapView.mNavigationPoints.get(i);

                Region region = new Region(
                        (int) (navigationPoint.drawX - size),
                        (int) (navigationPoint.drawY - size),
                        (int) (navigationPoint.drawX + size),
                        (int) (navigationPoint.drawY + size)
                );

                if (region.contains((int) mapXY[0], (int) mapXY[1])) {
                    if (mMapView.mSelectedNavigationPoints.contains(navigationPoint)) {
                        mMapView.mSelectedNavigationPoints.remove(navigationPoint);
                    } else {
                        mMapView.mSelectedNavigationPoints.add(navigationPoint);
                    }
                    if (mMapView.mBaseAdapter != null) {
                        mMapView.mBaseAdapter.notifyDataSetChanged();
                    }
                    mMapView.postInvalidate();
                    return true;
                }
            }
            return super.onSingleTapUp(e);
        }
    }
}
