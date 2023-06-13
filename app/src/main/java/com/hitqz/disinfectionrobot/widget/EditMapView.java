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

import java.util.ArrayList;
import java.util.List;

public class EditMapView extends View {
    private static final String TAG = "EditMapView";
    // 所有用户触发的缩放、平移等操作都通过下面的 Matrix 直接作用于画布上，
    // 将系统计算的一些初始缩放平移信息与用户操作的信息进行隔离，让操作更加直观
    private final Matrix mMatrix = new Matrix();
    private final Matrix mInvertMatrix = new Matrix();
    /**
     * 两指的中心点
     */
    private final PointF mid = new PointF();
    private PaintType paintType = PaintType.LINE;
    private int color = Color.BLACK;
    private int stroke = 5;

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
    private DrawType drawType = DrawType.DRAW;
    private int isTouchMode = 0;//1：缩放，2：移动
    private boolean isDrawMode = false;
    private PointF start = new PointF();
    // 初始的两个手指按下的触摸点的距离
    private float oriDis = 1f;
    private float MAX_SCALE = 1f;
    private float scaleSum = 1f;
    private float oldScale = 1f;

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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mOriginalMap == null) {
            return false;
        }

        switch (event.getAction() & MotionEvent.ACTION_MASK) {//多点要带 ACTION_MASK
            case MotionEvent.ACTION_DOWN: {
                if (!isDrawMode) {
                    start.set(event.getX(), event.getY());
                    isTouchMode = 2;
                }
            }
            break;
            case MotionEvent.ACTION_POINTER_DOWN: {//第二个手指按下
                if (!isDrawMode) {
                    start = getMid(event);
                    //当两指间距大于10时，计算两指中心点
                    oriDis = getDistance(event);
                    if (oriDis > 10f) {
                        isTouchMode = 1;
                    }
                }
            }
            break;
            case MotionEvent.ACTION_MOVE: {
                if (isTouchMode > 0) {
                    if (isTouchMode == 1) {
                        // 两个手指滑动
                        float newDist = getDistance(event);
                        Log.i(TAG, "onTouchEvent: newDist->" + newDist);
                        if (newDist > 10f) {
                            float scale = newDist / oriDis;
                            float temp = scaleSum * scale;

                            if (temp < 0.5f) {
                                scale = 0.5f / scaleSum;
                                temp = 0.5f;
                            }
                            scaleSum = temp;
                            if (Math.abs(oldScale - scale) / scale < 0.1f) {
                                break;
                            }
                            oldScale = scale;
                            mMatrix.postScale(scale, scale, mid.x, mid.y);
//                            mMatrix.getValues(mMatrixValues);
                            Log.d(TAG, "onTouchEvent: scale=====" + scale);
                        }
                    } else if (isTouchMode == 2) {
                        mMatrix.postTranslate(event.getX() - start.x, event.getY()
                                - start.y);
                        start.x = event.getX();
                        start.y = event.getY();
                    }
                    postInvalidate();
                } else if (isDrawMode) {
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
                        mLimitPath.color = color;
                        mLimitPath.width = stroke;
                        mLimitPath.drawType = drawType;
                        mLastX = x;
                        mLastY = y;
                        mLimitPath.path.moveTo(x, y);
                    }
                    if (paintType == PaintType.PATH) {
                        if (!mUndoList.contains(mLimitPath)) {
                            mUndoList.add(mLimitPath);
                            mRedoList.clear();
                        }
                        mLimitPath.path.quadTo(mLastX, mLastY, (x + mLastX) / 2, (y + mLastY) / 2);
                        mLastX = x;
                        mLastY = y;
                    }
                    if (paintType == PaintType.LINE) {
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
                isTouchMode = 0;
                mLimitPath = null;
                postInvalidate();
            }
            break;
            case MotionEvent.ACTION_POINTER_UP: {//第二个手指弹起
                isTouchMode = 0;
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
        scaleSum = 1f;
    }

    public void setLineType(PaintType type) {
        paintType = type;
    }

    public void setLineColor(int color) {
        this.color = color;
    }

    public void setStroke(int number) {
        stroke = number;
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
        Log.d(TAG, "width===" + width);
        Log.d(TAG, "height===" + height);

        this.mOriginalMap = map;
        MAX_SCALE = Math.min(width * 1f / mOriginalMap.getWidth(), height * 1f / mOriginalMap.getHeight());
        Log.d(TAG, "MAX_SCALE===" + MAX_SCALE);
        mid.set(width / 2f, height / 2f);
        mMatrix.setTranslate((width - mOriginalMap.getWidth()) / 2f, (height - mOriginalMap.getHeight()) / 2f);
        mMatrix.postScale(MAX_SCALE, MAX_SCALE, width / 2f, height / 2f);
        postInvalidate();
    }

    public Bitmap getOriginalMap() {
        return mOriginalMap;
    }

    public void setMode(boolean drawMode) {
        isDrawMode = drawMode;
        isTouchMode = 0;
    }

    public void setDrawType(DrawType drawType) {
        this.drawType = drawType;
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
