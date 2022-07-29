package com.hitqz.disinfectionrobot.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TaskState {

    @SerializedName("taskPosList")
    private List<TaskPosList> taskPosList;

    public List<TaskPosList> getTaskPosList() {
        return taskPosList;
    }

    public void setTaskPosList(List<TaskPosList> taskPosList) {
        this.taskPosList = taskPosList;
    }

    public static class TaskPosList {
        @SerializedName("name")
        private String name;
        @SerializedName("position")
        private Position position;
        @SerializedName("posState")
        private Integer posState;

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

        public Integer getPosState() {
            return posState;
        }

        public void setPosState(Integer posState) {
            this.posState = posState;
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
