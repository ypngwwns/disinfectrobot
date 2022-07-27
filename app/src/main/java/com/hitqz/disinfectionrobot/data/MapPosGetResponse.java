package com.hitqz.disinfectionrobot.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MapPosGetResponse {
    @SerializedName("mapPos_list")
    public List<MapPos> mapPos_list;
}
