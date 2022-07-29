package com.hitqz.disinfectionrobot.data;

public class MapBuildRequest {
    public String token;
    public int cmd;

    public MapBuildRequest(String token, int i) {
        this.token = token;
        this.cmd = i;
    }
}