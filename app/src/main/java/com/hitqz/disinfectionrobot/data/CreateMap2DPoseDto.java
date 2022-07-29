package com.hitqz.disinfectionrobot.data;

public class CreateMap2DPoseDto {

    private double x;

    private double y;

    private double z;

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

    @Override
    public String toString() {
        return "CreateMap2DPoseDto{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
