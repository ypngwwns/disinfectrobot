package com.hitqz.disinfectionrobot.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RobotStatus {

    @SerializedName("air")
    private AirDTO air;
    @SerializedName("currentPos")
    private CurrentPosDTO currentPos;
    @SerializedName("laserData")
    private List<LaserDataDTO> laserData;
    @SerializedName("powerInfo")
    private PowerInfoDTO powerInfo;

    public AirDTO getAir() {
        return air;
    }

    public void setAir(AirDTO air) {
        this.air = air;
    }

    public CurrentPosDTO getCurrentPos() {
        return currentPos;
    }

    public void setCurrentPos(CurrentPosDTO currentPos) {
        this.currentPos = currentPos;
    }

    public List<LaserDataDTO> getLaserData() {
        return laserData;
    }

    public void setLaserData(List<LaserDataDTO> laserData) {
        this.laserData = laserData;
    }

    public PowerInfoDTO getPowerInfo() {
        return powerInfo;
    }

    public void setPowerInfo(PowerInfoDTO powerInfo) {
        this.powerInfo = powerInfo;
    }

    public static class AirDTO {
        @SerializedName("co")
        private Integer co;
        @SerializedName("co2")
        private Integer co2;
        @SerializedName("formaldehyde")
        private Integer formaldehyde;
        @SerializedName("humidity")
        private Integer humidity;
        @SerializedName("o2")
        private Integer o2;
        @SerializedName("pm25")
        private Integer pm25;
        @SerializedName("temperature")
        private Integer temperature;
        @SerializedName("tvoc")
        private Integer tvoc;

        public Integer getCo() {
            return co;
        }

        public void setCo(Integer co) {
            this.co = co;
        }

        public Integer getCo2() {
            return co2;
        }

        public void setCo2(Integer co2) {
            this.co2 = co2;
        }

        public Integer getFormaldehyde() {
            return formaldehyde;
        }

        public void setFormaldehyde(Integer formaldehyde) {
            this.formaldehyde = formaldehyde;
        }

        public Integer getHumidity() {
            return humidity;
        }

        public void setHumidity(Integer humidity) {
            this.humidity = humidity;
        }

        public Integer getO2() {
            return o2;
        }

        public void setO2(Integer o2) {
            this.o2 = o2;
        }

        public Integer getPm25() {
            return pm25;
        }

        public void setPm25(Integer pm25) {
            this.pm25 = pm25;
        }

        public Integer getTemperature() {
            return temperature;
        }

        public void setTemperature(Integer temperature) {
            this.temperature = temperature;
        }

        public Integer getTvoc() {
            return tvoc;
        }

        public void setTvoc(Integer tvoc) {
            this.tvoc = tvoc;
        }
    }

    public static class CurrentPosDTO {
        @SerializedName("pitch")
        private Integer pitch;
        @SerializedName("roll")
        private Integer roll;
        @SerializedName("x")
        private Double x;
        @SerializedName("y")
        private Double y;
        @SerializedName("yaw")
        private Double yaw;
        @SerializedName("z")
        private Integer z;

        public Integer getPitch() {
            return pitch;
        }

        public void setPitch(Integer pitch) {
            this.pitch = pitch;
        }

        public Integer getRoll() {
            return roll;
        }

        public void setRoll(Integer roll) {
            this.roll = roll;
        }

        public Double getX() {
            return x;
        }

        public void setX(Double x) {
            this.x = x;
        }

        public Double getY() {
            return y;
        }

        public void setY(Double y) {
            this.y = y;
        }

        public Double getYaw() {
            return yaw;
        }

        public void setYaw(Double yaw) {
            this.yaw = yaw;
        }

        public Integer getZ() {
            return z;
        }

        public void setZ(Integer z) {
            this.z = z;
        }
    }

    public static class PowerInfoDTO {
        @SerializedName("capacitance")
        private Integer capacitance;
        @SerializedName("chargeStatus")
        private Integer chargeStatus;
        @SerializedName("current")
        private Double current;
        @SerializedName("currentDirection")
        private Integer currentDirection;
        @SerializedName("power")
        private Integer power;
        @SerializedName("robotSn")
        private String robotSn;
        @SerializedName("template")
        private Integer template;
        @SerializedName("voltage")
        private Double voltage;

        public Integer getCapacitance() {
            return capacitance;
        }

        public void setCapacitance(Integer capacitance) {
            this.capacitance = capacitance;
        }

        public Integer getChargeStatus() {
            return chargeStatus;
        }

        public void setChargeStatus(Integer chargeStatus) {
            this.chargeStatus = chargeStatus;
        }

        public Double getCurrent() {
            return current;
        }

        public void setCurrent(Double current) {
            this.current = current;
        }

        public Integer getCurrentDirection() {
            return currentDirection;
        }

        public void setCurrentDirection(Integer currentDirection) {
            this.currentDirection = currentDirection;
        }

        public Integer getPower() {
            return power;
        }

        public void setPower(Integer power) {
            this.power = power;
        }

        public String getRobotSn() {
            return robotSn;
        }

        public void setRobotSn(String robotSn) {
            this.robotSn = robotSn;
        }

        public Integer getTemplate() {
            return template;
        }

        public void setTemplate(Integer template) {
            this.template = template;
        }

        public Double getVoltage() {
            return voltage;
        }

        public void setVoltage(Double voltage) {
            this.voltage = voltage;
        }
    }

    public static class LaserDataDTO {
        @SerializedName("x")
        private Double x;
        @SerializedName("y")
        private Double y;
        @SerializedName("yaw")
        private Double yaw;
        @SerializedName("z")
        private Integer z;

        public Double getX() {
            return x;
        }

        public void setX(Double x) {
            this.x = x;
        }

        public Double getY() {
            return y;
        }

        public void setY(Double y) {
            this.y = y;
        }

        public Double getYaw() {
            return yaw;
        }

        public void setYaw(Double yaw) {
            this.yaw = yaw;
        }

        public Integer getZ() {
            return z;
        }

        public void setZ(Integer z) {
            this.z = z;
        }
    }
}
