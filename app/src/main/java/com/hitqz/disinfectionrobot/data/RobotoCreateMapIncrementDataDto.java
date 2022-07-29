package com.hitqz.disinfectionrobot.data;

import java.util.List;

/**
 * 机器人建图时候进行转换的增量数据
 */
public class RobotoCreateMapIncrementDataDto {

    private String robotSn;

    private int width;

    private float resolution;

    private int height;

    private List<Byte> bytes;

    private List<Integer> indices;

    private CreateMapPosInfoDto posInfoDto;

    private CreateMap2DRobotPoseDto robotInfoDto;

    public String getRobotSn() {
        return robotSn;
    }

    public void setRobotSn(String robotSn) {
        this.robotSn = robotSn;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public float getResolution() {
        return resolution;
    }

    public void setResolution(float resolution) {
        this.resolution = resolution;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public List<Byte> getBytes() {
        return bytes;
    }

    public void setBytes(List<Byte> bytes) {
        this.bytes = bytes;
    }

    public List<Integer> getIndices() {
        return indices;
    }

    public void setIndices(List<Integer> indices) {
        this.indices = indices;
    }

    public CreateMapPosInfoDto getPosInfoDto() {
        return posInfoDto;
    }

    public void setPosInfoDto(CreateMapPosInfoDto posInfoDto) {
        this.posInfoDto = posInfoDto;
    }

    public CreateMap2DRobotPoseDto getRobotInfoDto() {
        return robotInfoDto;
    }

    public void setRobotInfoDto(CreateMap2DRobotPoseDto robotInfoDto) {
        this.robotInfoDto = robotInfoDto;
    }

    @Override
    public String toString() {
        return "RobotoCreateMapIncrementDataDto{" +
                "robotSn='" + robotSn + '\'' +
                ", width=" + width +
                ", resolution=" + resolution +
                ", height=" + height +
                ", bytes=" + bytes +
                ", indices=" + indices +
                ", posInfoDto=" + posInfoDto +
                ", robotInfoDto=" + robotInfoDto +
                '}';
    }
}
