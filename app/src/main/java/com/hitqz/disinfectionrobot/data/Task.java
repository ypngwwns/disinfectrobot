package com.hitqz.disinfectionrobot.data;

public class Task {

    public String jobTime;
    public Integer jobStatus;
    public Integer taskType;
    public String areaName;
    public WorkArea workArea;
    public Integer id;

    public static class WorkArea {
        public String areaName;
        public Long createTime;
        public Long lastModifyTime;
        public Integer id;
        public String type;
        public Integer version;
        public String mapCode;
        public String status;
    }
}
