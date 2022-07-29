package com.hitqz.disinfectionrobot.data;

public class MapUploadRequest {
    public String token;
    public String mapName;

    public MapUploadRequest(String token, String text) {
        this.token = token;
        this.mapName = text;
    }
}