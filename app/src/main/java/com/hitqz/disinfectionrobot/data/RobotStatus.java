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
    @SerializedName("laserOriginData")
    private List<LaserOriginDataDTO> laserOriginData;
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

    public List<LaserOriginDataDTO> getLaserOriginData() {
        return laserOriginData;
    }

    public void setLaserOriginData(List<LaserOriginDataDTO> laserOriginData) {
        this.laserOriginData = laserOriginData;
    }

    public PowerInfoDTO getPowerInfo() {
        return powerInfo;
    }

    public void setPowerInfo(PowerInfoDTO powerInfo) {
        this.powerInfo = powerInfo;
    }

    public static class AirDTO {
        @SerializedName("co")
        private int co;
        @SerializedName("co2")
        private int co2;
        @SerializedName("formaldehyde")
        private int formaldehyde;
        @SerializedName("humidity")
        private int humidity;
        @SerializedName("o2")
        private int o2;
        @SerializedName("pm25")
        private int pm25;
        @SerializedName("temperature")
        private int temperature;
        @SerializedName("tvoc")
        private int tvoc;

        public int getCo() {
            return co;
        }

        public void setCo(int co) {
            this.co = co;
        }

        public int getCo2() {
            return co2;
        }

        public void setCo2(int co2) {
            this.co2 = co2;
        }

        public int getFormaldehyde() {
            return formaldehyde;
        }

        public void setFormaldehyde(int formaldehyde) {
            this.formaldehyde = formaldehyde;
        }

        public int getHumidity() {
            return humidity;
        }

        public void setHumidity(int humidity) {
            this.humidity = humidity;
        }

        public int getO2() {
            return o2;
        }

        public void setO2(int o2) {
            this.o2 = o2;
        }

        public int getPm25() {
            return pm25;
        }

        public void setPm25(int pm25) {
            this.pm25 = pm25;
        }

        public int getTemperature() {
            return temperature;
        }

        public void setTemperature(int temperature) {
            this.temperature = temperature;
        }

        public int getTvoc() {
            return tvoc;
        }

        public void setTvoc(int tvoc) {
            this.tvoc = tvoc;
        }
    }

    public static class CurrentPosDTO {
        @SerializedName("pitch")
        private int pitch;
        @SerializedName("roll")
        private int roll;
        @SerializedName("x")
        private double x;
        @SerializedName("y")
        private double y;
        @SerializedName("yaw")
        private double yaw;
        @SerializedName("z")
        private int z;

        public int getPitch() {
            return pitch;
        }

        public void setPitch(int pitch) {
            this.pitch = pitch;
        }

        public int getRoll() {
            return roll;
        }

        public void setRoll(int roll) {
            this.roll = roll;
        }

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

        public double getYaw() {
            return yaw;
        }

        public void setYaw(double yaw) {
            this.yaw = yaw;
        }

        public int getZ() {
            return z;
        }

        public void setZ(int z) {
            this.z = z;
        }
    }

    public static class PowerInfoDTO {
        @SerializedName("capacitance")
        private int capacitance;
        @SerializedName("chargeStatus")
        private int chargeStatus;
        @SerializedName("current")
        private double current;
        @SerializedName("currentDirection")
        private int currentDirection;
        @SerializedName("power")
        private int power;
        @SerializedName("robotSn")
        private String robotSn;
        @SerializedName("template")
        private int template;
        @SerializedName("voltage")
        private double voltage;

        public int getCapacitance() {
            return capacitance;
        }

        public void setCapacitance(int capacitance) {
            this.capacitance = capacitance;
        }

        public int getChargeStatus() {
            return chargeStatus;
        }

        public void setChargeStatus(int chargeStatus) {
            this.chargeStatus = chargeStatus;
        }

        public double getCurrent() {
            return current;
        }

        public void setCurrent(double current) {
            this.current = current;
        }

        public int getCurrentDirection() {
            return currentDirection;
        }

        public void setCurrentDirection(int currentDirection) {
            this.currentDirection = currentDirection;
        }

        public int getPower() {
            return power;
        }

        public void setPower(int power) {
            this.power = power;
        }

        public String getRobotSn() {
            return robotSn;
        }

        public void setRobotSn(String robotSn) {
            this.robotSn = robotSn;
        }

        public int getTemplate() {
            return template;
        }

        public void setTemplate(int template) {
            this.template = template;
        }

        public double getVoltage() {
            return voltage;
        }

        public void setVoltage(double voltage) {
            this.voltage = voltage;
        }
    }

    public static class LaserDataDTO {
        @SerializedName("x")
        private double x;
        @SerializedName("y")
        private double y;
        @SerializedName("yaw")
        private double yaw;
        @SerializedName("z")
        private int z;

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

        public double getYaw() {
            return yaw;
        }

        public void setYaw(double yaw) {
            this.yaw = yaw;
        }

        public int getZ() {
            return z;
        }

        public void setZ(int z) {
            this.z = z;
        }
    }

    public static class LaserOriginDataDTO {
        @SerializedName("x")
        private double x;
        @SerializedName("y")
        private double y;
        @SerializedName("yaw")
        private double yaw;
        @SerializedName("z")
        private int z;

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

        public double getYaw() {
            return yaw;
        }

        public void setYaw(double yaw) {
            this.yaw = yaw;
        }

        public int getZ() {
            return z;
        }

        public void setZ(int z) {
            this.z = z;
        }
    }
}
