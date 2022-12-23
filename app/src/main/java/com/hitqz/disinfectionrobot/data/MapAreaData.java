package com.hitqz.disinfectionrobot.data;

import java.util.List;

public class MapAreaData {

    public List<Action> mActions;
    public String areaName;

    public static class Action {
        public Integer cmd;
        public Integer id;
    }
}
