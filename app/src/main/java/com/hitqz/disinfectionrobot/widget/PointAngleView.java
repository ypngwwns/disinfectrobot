package com.hitqz.disinfectionrobot.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.hitqz.disinfectionrobot.R;

public class PointAngleView extends View {
    private float currentAngle = 0.0f;
    private PointF selectedPointf = new PointF();
    private GestureDetector gestureDetector;

    private Paint pointPaint;

    private int viewWidth;
    private int viewHeigth;

    private int pointSize;

    private EditText pointNameText;

    public PointAngleView(Context context) {
        super(context);
    }

    public PointAngleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        gestureDetector = new GestureDetector(getContext(), new NaviPointAngleGentureListenner(this));
        initPaint();
    }

    public PointAngleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        gestureDetector = new GestureDetector(getContext(), new NaviPointAngleGentureListenner(this));
        initPaint();
    }

    public PointAngleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        gestureDetector = new GestureDetector(getContext(), new NaviPointAngleGentureListenner(this));
        initPaint();
    }

    private void initPaint() {
        pointPaint = new Paint();             // 创建画笔
        pointPaint.setColor(getResources().getColor(R.color.colorAccent, null));           // 画笔颜色 - 黑色
        pointPaint.setStyle(Paint.Style.FILL);    // 填充模式 - 填充
        pointPaint.setStrokeWidth(10);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeigth = h;

        selectedPointf.x = w / 2;
        selectedPointf.y = h / 2;

        pointSize = (w > h ? h : w) / 3;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 绘制图像
        canvas.save();

        canvas.translate(viewWidth / 2, viewHeigth / 2);

        // 绘制图形时角度要取负数
        // 安卓是以顺时针为坐标系
        canvas.rotate(-currentAngle);

        Path path = new Path();
        path.moveTo(pointSize, 0);
        path.lineTo(-pointSize, -pointSize);
        path.lineTo(-pointSize / 2, 0);
        path.lineTo(-pointSize, pointSize);
        path.lineTo(pointSize, 0);

        canvas.drawPath(path, pointPaint);
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return true;
    }

    public float getCurrentAngle() {
        return currentAngle;
    }

    public void setCurrentAngle(float currentAngle) {
        this.currentAngle = currentAngle;
    }

    public PointF getSelectedPointf() {
        return selectedPointf;
    }

    public void setSelectedPointf(PointF selectedPointf) {
        this.selectedPointf = selectedPointf;
    }
}
