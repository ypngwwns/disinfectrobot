package com.hitqz.disinfectionrobot.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MapArea {

    @SerializedName("mapAreaName")
    private String mapAreaName;
    @SerializedName("mapAreaPosList")
    private List<MapAreaPos> mapAreaPosList;

    public String getMapAreaName() {
        return mapAreaName;
    }

    public void setMapAreaName(String mapAreaName) {
        this.mapAreaName = mapAreaName;
    }

    public List<MapAreaPos> getMapAreaPosList() {
        return mapAreaPosList;
    }

    public void setMapAreaPosList(List<MapAreaPos> mapAreaPosList) {
        this.mapAreaPosList = mapAreaPosList;
    }

    public static class MapAreaPos {
        @SerializedName("name")
        private String name;
        @SerializedName("position")
        private Position position;
        @SerializedName("disinfection")
        private Integer disinfection;
        @SerializedName("stay_time")
        private String stayTime;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
