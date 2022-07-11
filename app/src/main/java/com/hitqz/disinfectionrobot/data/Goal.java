package com.hitqz.disinfectionrobot.data;

import com.google.gson.annotations.SerializedName;

public class Goal {

    @SerializedName("posId")
    private Integer posId;
    @SerializedName("posx")
    private Double posx;
    @SerializedName("posy")
    private Double posy;
    @SerializedName("posz")
    private Object posz;
    @SerializedName("yaw")
    private Double yaw;
    @SerializedName("type")
    private String type;
    @SerializedName("deviceName")
    private String deviceName;
    @SerializedName("deviceId")
    private Integer deviceId;

    public Integer getPosId() {
        return posId;
    }

    public void setPosId(Integer posId) {
        this.posId = posId;
    }

    public Double getPosx() {
        return posx;
    }

    public void setPosx(Double posx) {
        this.posx = posx;
    }

    public Double getPosy() {
        return posy;
    }

    public void setPosy(Double posy) {
        this.posy = posy;
    }

    public Object getPosz() {
        return posz;
    }

    public void setPosz(Object posz) {
        this.posz = posz;
    }

    public Double getYaw() {
        return yaw;
    }

    public void setYaw(Double yaw) {
        this.yaw = yaw;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }
}
