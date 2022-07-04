package com.hitqz.disinfectionrobot.util;

import android.graphics.PointF;

public class AngleUtil {
    private static final String TAG = "AngleUtil";

    public static float calcAngle(PointF touchPoint, PointF centerPoint) {
        // 计算角度
        double radian = calcRadian(touchPoint, centerPoint);
        double angle = radian2Angle(radian);

//        double calcRadian = angle2radian(angle);
//        String detail = " radian: " + radian + " angle: " + angle + " calc: " + calcRadian;
//        LogUtil.w(TAG, detail);

        return (float)angle;
    }

    public static float calcRadian(PointF touchPoint, PointF centerPoint) {
        float lenX = touchPoint.x - centerPoint.x;
        // 两点在Y轴距离
        float lenY = centerPoint.y - touchPoint.y;
        // 两点距离
        float lenXY = (float) Math.sqrt((double) (lenX * lenX + lenY * lenY));
        // 计算弧度
//        double radian = Math.acos(lenX / lenXY) * (touchPoint.y > centerPoint.y ? -1 : 1);

        double radian = Math.atan2(lenY, lenX);

        return (float)radian;
    }

    public static double radian2Angle(double radian) {
        double tmp = Math.round(radian / Math.PI * 180);
        return tmp > 0 ? tmp : 360 + tmp;
    }

    public static double angle2radian(double angle) {
        double calcAngle = angle;
        if (calcAngle > 180) {
            calcAngle = angle - 360;
        }
        return calcAngle * (Math.PI / 180);
    }
}
