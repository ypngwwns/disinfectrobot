package com.hitqz.disinfectionrobot.data;

public class Task {

    public String jobTime;
    public int jobStatus;
    public int taskType;
    public String areaName;
    public WorkArea workArea;
    public int id;

    public static class WorkArea {
        public String areaName;
        public Long createTime;
        public Long lastModifyTime;
        public int id;
        public String type;
        public int version;
        public String mapCode;
        public String status;
    }
}
