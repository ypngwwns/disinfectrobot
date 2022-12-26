package com.hitqz.disinfectionrobot.data;

import org.litepal.crud.LitePalSupport;

import java.util.List;

public class NavigationPoint extends LitePalSupport {

    public int id;
    public String type;
    public int action;
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

    public static NavigationPoint convertFromAreaPose(List<NavigationPoint> navigationPoints, AreaPose areaPose) {
        for (NavigationPoint n : navigationPoints) {
            if (n.id == areaPose.id) {
                n.action = areaPose.action;
                return n;
            }
        }
        return null;
    }
}
