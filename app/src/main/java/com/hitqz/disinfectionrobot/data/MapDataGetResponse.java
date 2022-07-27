package com.hitqz.disinfectionrobot.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MapDataGetResponse {

    @SerializedName("MapData")
    private MapData mapData;

    public MapData getMapData() {
        return mapData;
    }

    public void setMapData(MapData mapData) {
        this.mapData = mapData;
    }

    public static class MapData {
        @SerializedName("name")
        private String name;
        @SerializedName("pgm_size")
        private int pgmSize;
        @SerializedName("pgm_data_compress")
        private List<Byte> pgmDataCompress;
        @SerializedName("yaml_size")
        private int yamlSize;
        @SerializedName("yaml_data")
        private List<Byte> yamlData;
        @SerializedName("width")
        private int width;
        @SerializedName("height")
        private int height;
        @SerializedName("origin_x")
        private float originX;
        @SerializedName("origin_y")
        private float originY;
        @SerializedName("resolution ")
        private float resolution;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPgmSize() {
            return pgmSize;
        }

        public void setPgmSize(int pgmSize) {
            this.pgmSize = pgmSize;
        }

        public List<Byte> getPgmDataCompress() {
            return pgmDataCompress;
        }

        public void setPgmDataCompress(List<Byte> pgmDataCompress) {
            this.pgmDataCompress = pgmDataCompress;
        }

        public int getYamlSize() {
            return yamlSize;
        }

        public void setYamlSize(int yamlSize) {
            this.yamlSize = yamlSize;
        }

        public List<Byte> getYamlData() {
            return yamlData;
        }

        public void setYamlData(List<Byte> yamlData) {
            this.yamlData = yamlData;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public float getOriginX() {
            return originX;
        }

        public void setOriginX(float originX) {
            this.originX = originX;
        }

        public float getOriginY() {
            return originY;
        }

        public void setOriginY(float originY) {
            this.originY = originY;
        }

        public float getResolution() {
            return resolution;
        }

        public void setResolution(float resolution) {
            this.resolution = resolution;
        }
    }
}
