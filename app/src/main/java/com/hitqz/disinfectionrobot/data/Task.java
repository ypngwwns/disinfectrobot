package com.hitqz.disinfectionrobot.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Task {

    @SerializedName("taskName")
    private String taskName;
    @SerializedName("runTime")
    private String runTime;
    @SerializedName("taskState")
    private String taskState;
    @SerializedName("taskAreaList")
    private List<String> taskAreaList;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getRunTime() {
        return runTime;
    }

    public void setRunTime(String runTime) {
        this.runTime = runTime;
    }

    public String getTaskState() {
        return taskState;
    }

    public void setTaskState(String taskState) {
        this.taskState = taskState;
    }

    public List<String> getTaskAreaList() {
        return taskAreaList;
    }

    public void setTaskAreaList(List<String> taskAreaList) {
        this.taskAreaList = taskAreaList;
    }
}
