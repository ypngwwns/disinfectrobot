package com.hitqz.disinfectionrobot.widget;

import android.graphics.PointF;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.hitqz.disinfectionrobot.util.AngleUtil;

public class NaviPointAngleGentureListenner extends GestureDetector.SimpleOnGestureListener {
    private final PointAngleView view;

    public NaviPointAngleGentureListenner(PointAngleView view) {
        this.view = view;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        PointF originPoint = view.getSelectedPointf();
        PointF touchPoint = new PointF(e2.getX(), e2.getY());
        float currentAngle = (AngleUtil.calcAngle(touchPoint, originPoint));
        view.setCurrentAngle(currentAngle);
        view.invalidate();
        return false;
    }
}
