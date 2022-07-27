package com.hitqz.disinfectionrobot.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MapAreaSetRequest {

    @SerializedName("mapAreaName")
    private String mapAreaName;
    @SerializedName("mapAreaPosList")
    private List<MapAreaPosList> mapAreaPosList;

    public String getMapAreaName() {
        return mapAreaName;
    }

    public void setMapAreaName(String mapAreaName) {
        this.mapAreaName = mapAreaName;
    }

    public List<MapAreaPosList> getMapAreaPosList() {
        return mapAreaPosList;
    }

    public void setMapAreaPosList(List<MapAreaPosList> mapAreaPosList) {
        this.mapAreaPosList = mapAreaPosList;
    }

    public static class MapAreaPosList {
        @SerializedName("name")
        private String name;
        @SerializedName("position")
        private Position position;
        @SerializedName("disinfection")
        private int disinfection;
        @SerializedName("stay_time")
        private String stayTime;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Position getPosition() {
            return position;
        }

        public void setPosition(Position position) {
            this.position = position;
        }

        public int getDisinfection() {
            return disinfection;
        }

        public void setDisinfection(int disinfection) {
            this.disinfection = disinfection;
        }

        public String getStayTime() {
            return stayTime;
        }

        public void setStayTime(String stayTime) {
            this.stayTime = stayTime;
        }

        public static class Position {
            @SerializedName("x")
            private String x;
            @SerializedName("y")
            private String y;
            @SerializedName("yaw")
            private String yaw;

            public String getX() {
                return x;
            }

            public void setX(String x) {
                this.x = x;
            }

            public String getY() {
                return y;
            }

            public void setY(String y) {
                this.y = y;
            }

            public String getYaw() {
                return yaw;
            }

            public void setYaw(String yaw) {
                this.yaw = yaw;
            }
        }
    }
}
