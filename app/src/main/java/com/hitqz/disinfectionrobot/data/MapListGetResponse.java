package com.hitqz.disinfectionrobot.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MapListGetResponse {

    @SerializedName("“mapName_list”")
    private List<String> mapName_list;// FIXME check this code

    public List<String> getMapNameList() {
        return mapName_list;
    }

    public void setMapNameList(List<String> mapNameList) {
        this.mapName_list = mapNameList;
    }
}
