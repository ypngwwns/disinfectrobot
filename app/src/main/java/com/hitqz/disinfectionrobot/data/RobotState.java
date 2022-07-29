package com.hitqz.disinfectionrobot.data;

import com.google.gson.annotations.SerializedName;

public class RobotState {

    @SerializedName("power")
    private int power;
    @SerializedName("state")
    private int state;
    @SerializedName("level")
    private int level;
    @SerializedName("voltage")
    private int voltage;
    @SerializedName("current")
    private int current;

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getVoltage() {
        return voltage;
    }

    public void setVoltage(int voltage) {
        this.voltage = voltage;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }
}
