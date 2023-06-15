package com.hitqz.disinfectionrobot.net.data;

import com.google.gson.annotations.SerializedName;

public class CleanTask {

    public int id;
    @SerializedName("endTime")
    public String endTime;
    @SerializedName("jobStatus")
    public int jobStatus;
    @SerializedName("startTime")
    public String startTime;
    @SerializedName("taskName")
    public String taskName;
    @SerializedName("taskType")
    public int taskType;
}
