package com.hitqz.disinfectionrobot.data;

import java.util.List;

public class MapAreaData {

    public List<Action> actions;
    public String areaName;

    public static class Action {
        public Integer cmd;
        public Integer id;
    }
}
