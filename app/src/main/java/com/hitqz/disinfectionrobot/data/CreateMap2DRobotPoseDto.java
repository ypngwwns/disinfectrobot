package com.hitqz.disinfectionrobot.data;

import org.jetbrains.annotations.NotNull;

public class CreateMap2DRobotPoseDto {

    private double x;

    private double y;

    private double z;

    private double yaw;

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double getYaw() {
        return yaw;
    }

    public void setYaw(double yaw) {
        this.yaw = yaw;
    }

    @Override
    public String toString() {
        return "CreateMap2DRobotPoseDto{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", yaw=" + yaw +
                '}';
    }
}
