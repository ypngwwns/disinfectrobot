package com.hitqz.disinfectionrobot.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.math.MathUtils;

import java.util.ArrayList;
import java.util.List;

public class EditMapView extends View {
    public static final float MAX_SCALE_FACTER = 5f;
    public static final float MIN_SCALE_FACTER = 0.5f;
    private static final String TAG = EditMapView.class.getSimpleName();
    // 所有用户触发的缩放、平移等操作都通过下面的 Matrix 直接作用于画布上，
    // 将系统计算的一些初始缩放平移信息与用户操作的信息进行隔离，让操作更加直观
    private final Matrix mMatrix = new Matrix();
    private final Matrix mInvertMatrix = new Matrix();
    /**
     * 两指的中心点
     */
    private final PointF mMid = new PointF();
    /**
     * 平移缩放变化的计算值
     */
//    private final float[] mMatrixValues = new float[9];
    private PaintType mPaintType = PaintType.LINE;
    private int mBlack = Color.BLACK;
    private int mStroke = 5;
    /**
     * 画布
     */
    private Canvas mCanvas;
    private Paint mPaint = new Paint();
    private Paint mEarsePaint = new Paint();
    /**
     * 原始地图
     */
    private Bitmap mOriginalMap = null;
    /**
     * 生成的地图
     */
    private Bitmap mChangeMap = null;
    /**
     * 最终滑动点
     */
    private float mLastX, mLastY;
    /**
     * 绘制虚拟墙曲线所需
     */
    private LimitPath mLimitPath;
    /**
     * 保存绘制的曲线
     */
    private List<LimitPath> mUndoList = new ArrayList<>();
    private List<LimitPath> mRedoList = new ArrayList<>();
    /**
     * 强力擦除模式
     */
    private DrawType mDrawType = DrawType.DRAW;
    private int mIsTouchMode = 0;//1：缩放，2：移动
    private boolean mIsDrawMode = false;
    private PointF mStart = new PointF();
    // 初始的两个手指按下的触摸点的距离
    private float mOriDis = 1f;
    private float mScaleSum = 1f;

    public EditMapView(Context context) {
        super(context);
        init(context);
    }

    public EditMapView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init(context);
    }

    public EditMapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 取两指的中心点坐标
     */
    public static PointF getMid(MotionEvent event) {
        float midX = (event.getX(1) + event.getX(0)) / 2;
        float midY = (event.getY(1) + event.getY(0)) / 2;
        return new PointF(midX, midY);
    }

    private void init(Context context) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);

        mEarsePaint = new Paint();
        mEarsePaint.setAntiAlias(true);
        mEarsePaint.setStyle(Paint.Style.STROKE);
        mEarsePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
//        setBackgroundColor(getResources().getColor(R.color.laserLineColor));
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.setMatrix(mMatrix);
        if (mOriginalMap != null) {
            canvas.drawBitmap(mOriginalMap, 0, 0, mPaint);
        }
        canvas.restore();
        int layerId = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
        canvas.setMatrix(mMatrix);
        for (LimitPath limit : mUndoList) {
            Path p = limit.path;
            mPaint.setColor(limit.color);
            mPaint.setStrokeWidth(limit.width);
            mEarsePaint.setStrokeWidth(limit.width);
            switch (limit.drawType) {
                case DRAW:
                case WHITE:
                    canvas.drawPath(p, mPaint);
                    break;
                case ERASE:
                    canvas.drawPath(p, mEarsePaint);
                    break;
            }
        }
        canvas.restoreToCount(layerId);
    }

    private float clampScale(float scale) {
        float max = MAX_SCALE_FACTER / mScaleSum;
        float min = MIN_SCALE_FACTER / mScaleSum;
        return MathUtils.clamp(scale, min, max);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mOriginalMap == null) {
            return false;
        }

        switch (event.getAction() & MotionEvent.ACTION_MASK) {//多点要带 ACTION_MASK
            case MotionEvent.ACTION_DOWN: {
                if (!mIsDrawMode) {
                    mStart.set(event.getX(), event.getY());
                    mIsTouchMode = 2;
                }
            }
            break;
            case MotionEvent.ACTION_POINTER_DOWN: {//第二个手指按下
                if (!mIsDrawMode) {
                    mStart = getMid(event);
                    //当两指间距大于10时，计算两指中心点
                    mOriDis = getDistance(event);
                    if (mOriDis > 10f) {
                        mIsTouchMode = 1;
                    }
                }
            }
            break;
            case MotionEvent.ACTION_MOVE: {
                if (mIsTouchMode > 0) {
                    if (mIsTouchMode == 1) {
                        // 两个手指滑动
                        float newDist = getDistance(event);
                        Log.i(TAG, "onTouchEvent: newDist->" + newDist);
                        if (newDist > 10f) {
                            float scale = newDist / mOriDis;
                            scale = clampScale(scale);
                            mMatrix.postScale(scale, scale, mMid.x, mMid.y);
                            mScaleSum *= scale;
                            mOriDis = newDist;
                            Log.d(TAG, "onTouchEvent: scale=====" + scale);
                        }
                    } else if (mIsTouchMode == 2) {
                        mMatrix.postTranslate(event.getX() - mStart.x, event.getY()
                                - mStart.y);
                        mStart.x = event.getX();
                        mStart.y = event.getY();
                    }
                    postInvalidate();
                } else if (mIsDrawMode) {
                    float x = event.getX();
                    float y = event.getY();
                    float[] tempPoints = new float[]{x, y};
                    boolean result = mMatrix.invert(mInvertMatrix);
//                    Log.d(TAG, "result===" + result);
                    mInvertMatrix.mapPoints(tempPoints);
                    x = tempPoints[0];
                    y = tempPoints[1];
                    if (mLimitPath == null) {
                        mLimitPath = new LimitPath();
                        mLimitPath.color = mBlack;
                        mLimitPath.width = mStroke;
                        mLimitPath.drawType = mDrawType;
                        mLastX = x;
                        mLastY = y;
                        mLimitPath.path.moveTo(x, y);
                    }
                    if (mPaintType == PaintType.PATH) {
                        if (!mUndoList.contains(mLimitPath)) {
                            mUndoList.add(mLimitPath);
                            mRedoList.clear();
                        }
                        mLimitPath.path.quadTo(mLastX, mLastY, (x + mLastX) / 2, (y + mLastY) / 2);
                        mLastX = x;
                        mLastY = y;
                    }
                    if (mPaintType == PaintType.LINE) {
                        if (!mUndoList.contains(mLimitPath)) {
                            mUndoList.add(mLimitPath);
                            mRedoList.clear();
                        }

                        Path line = new Path();
                        line.moveTo(mLastX, mLastY);
                        line.lineTo(x, y);
                        mLimitPath.path.set(line);
                    }
                    postInvalidate();
                }
            }
            break;
            case MotionEvent.ACTION_UP: {
                mIsTouchMode = 0;
                mLimitPath = null;
                postInvalidate();
            }
            break;
            case MotionEvent.ACTION_POINTER_UP: {//第二个手指弹起
                mIsTouchMode = 0;
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

    private void reset() {
        if (mUndoList == null) {
            mUndoList = new ArrayList<>();
        } else {
            mUndoList.clear();
        }
        if (mRedoList == null) {
            mRedoList = new ArrayList<>();
        } else {
            mRedoList.clear();
        }

        mMatrix.reset();
        mCanvas = null;
        mScaleSum = 1f;
    }

    public void setLineType(PaintType type) {
        mPaintType = type;
    }

    public void setLineColor(int color) {
        this.mBlack = color;
    }

    public void setStroke(int number) {
        mStroke = number;
    }

    public void undo() {
        if (mUndoList != null) {
            if (mUndoList.size() > 0) {
                mRedoList.add(0, mUndoList.get(mUndoList.size() - 1));
                mUndoList.remove(mUndoList.size() - 1);
                postInvalidate();
            }
        }
    }

    public void redo() {
        if (mRedoList != null) {
            if (mRedoList.size() > 0) {
                mUndoList.add(mRedoList.get(0));
                mRedoList.remove(0);
                postInvalidate();
            }
        }
    }

    public Bitmap getMap() {
        if (mCanvas == null) {
            mChangeMap = Bitmap.createBitmap(mOriginalMap).copy(Bitmap.Config.ARGB_8888, true);//新建一个位图
            mCanvas = new Canvas(mChangeMap);
            mCanvas.drawBitmap(mOriginalMap, 0, 0, null);
            int canvasWidth = mCanvas.getWidth();
            int canvasHeight = mCanvas.getHeight();
            int layerId = mCanvas.saveLayer(0, 0, canvasWidth, canvasHeight, null, Canvas.ALL_SAVE_FLAG);
            for (LimitPath limit : mUndoList) {
                Path p = limit.path;
                mPaint.setColor(limit.color);
                mPaint.setStrokeWidth(limit.width);
                mEarsePaint.setStrokeWidth(limit.width);
                switch (limit.drawType) {
                    case DRAW:
                    case WHITE:
                        mCanvas.drawPath(p, mPaint);
                        break;
                    case ERASE:
                        mCanvas.drawPath(p, mEarsePaint);
                        break;
                }
            }
            mCanvas.restoreToCount(layerId);
        }
        return mChangeMap;
    }

    public void setMap(Bitmap map) {
        reset();
        float width = getWidth();
        float height = getHeight();

        this.mOriginalMap = map;
        mMid.set(width / 2f, height / 2f);
        mMatrix.setTranslate((width - mOriginalMap.getWidth()) / 2f, (height - mOriginalMap.getHeight()) / 2f);
        postInvalidate();
    }

    public Bitmap getOriginalMap() {
        return mOriginalMap;
    }

    public void setMode(boolean drawMode) {
        mIsDrawMode = drawMode;
        mIsTouchMode = 0;
    }

    public void setDrawType(DrawType drawType) {
        this.mDrawType = drawType;
    }

    public boolean operated() {
        return mUndoList.size() > 0 || mRedoList.size() > 0;
    }

    public enum PaintType {
        LINE,//直线
        PATH,//曲线
    }

    public enum DrawType {
        DRAW,
        WHITE,
        ERASE
    }

    /**
     * 虚拟墙数据
     */
    public static class LimitPath {
        private final Path path = new Path();
        private int width = 5;
        private int color = Color.BLACK;
        private DrawType drawType = DrawType.DRAW;

        public LimitPath() {
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }
    }
}
