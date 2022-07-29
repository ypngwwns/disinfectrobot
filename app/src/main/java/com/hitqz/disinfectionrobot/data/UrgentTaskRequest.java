package com.hitqz.disinfectionrobot.data;

import java.util.List;

public class UrgentTaskRequest {

    public String token;

    //传空代表获取全部
    public String scheduleName;

    public List<String> taskAreaList;
}
