package com.hitqz.disinfectionrobot.data;

import com.google.gson.annotations.SerializedName;

public class Position {

    @SerializedName("x")
    public double x;
    @SerializedName("y")
    public double y;
    @SerializedName("yaw")
    public double yaw;

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
