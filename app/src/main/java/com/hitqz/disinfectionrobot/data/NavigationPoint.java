package com.hitqz.disinfectionrobot.data;

import org.litepal.crud.LitePalSupport;

public class NavigationPoint extends LitePalSupport {

    public String mapCode;
    public String name;

    public double rawX;
    public double rawY;
    public double radian;

    public float drawX;
    public float drawY;
    public float angle;

}
