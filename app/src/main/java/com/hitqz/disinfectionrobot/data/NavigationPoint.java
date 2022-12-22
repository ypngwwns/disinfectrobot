package com.hitqz.disinfectionrobot.data;

import org.litepal.crud.LitePalSupport;

public class NavigationPoint extends LitePalSupport {

    public int id;
    public String type;
    public String mapCode;
    public String name;

    public double rawX;
    public double rawY;
    public double radian;

    public float drawX;
    public float drawY;
    public float angle;

    public static NavigationPoint convertFromMapPose(MapPose mapPose) {
        NavigationPoint navigationPoint = new NavigationPoint();
        navigationPoint.mapCode = mapPose.mapCode;
        navigationPoint.name = mapPose.name;
        navigationPoint.rawX = mapPose.posx;
        navigationPoint.rawY = mapPose.posy;
        navigationPoint.radian = mapPose.yaw;
        navigationPoint.id = mapPose.id;
        navigationPoint.type = mapPose.type;
        return navigationPoint;
    }
}
