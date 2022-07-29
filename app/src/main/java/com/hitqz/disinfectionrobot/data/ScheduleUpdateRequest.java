package com.hitqz.disinfectionrobot.data;

import java.util.List;

public class ScheduleUpdateRequest {

    public String token;

    //传空代表获取全部
    public String scheduleName;

    public List<Task> taskList;
}
