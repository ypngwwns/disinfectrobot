package com.hitqz.disinfectionrobot.net;

import com.hitqz.disinfectionrobot.data.AreaId;
import com.hitqz.disinfectionrobot.data.AreaPose;
import com.hitqz.disinfectionrobot.data.Cmd;
import com.hitqz.disinfectionrobot.data.LoginRequest;
import com.hitqz.disinfectionrobot.data.LoginResponse;
import com.hitqz.disinfectionrobot.data.MapArea;
import com.hitqz.disinfectionrobot.data.MapAreaData;
import com.hitqz.disinfectionrobot.data.MapDataResponse;
import com.hitqz.disinfectionrobot.data.MapPose;
import com.hitqz.disinfectionrobot.data.PointData;
import com.hitqz.disinfectionrobot.data.SpeedRequest;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ISkyNet {

    /**
     * 登录
     *
     * @return List<VestBean>
     */
    @POST("/robot/api/login")
    Observable<BaseRespond<LoginResponse>> login(@Body LoginRequest request);

    /**
     * 手动速度控制
     */
    @POST("/robot/api/ctrl/move")
    Observable<BaseRespond<Object>> ctrlMove(@Body SpeedRequest request);

    /**
     * 个手动开启喷雾和关闭喷雾
     */
    @POST("/robot/api/ctrl/disinfectCmd")
    Observable<BaseRespond<Object>> disinfectCmd(@Body Cmd cmd);

    /**
     * 获取当前使用地图
     */
    @GET("/robot/api/ctrl/mapCurGet")
    Observable<BaseRespond<MapDataResponse>> mapCurGet();

    /**
     * 获取地图上的点位
     */
    @POST("/robot/api/mapPos/mapPosListGet")
    Observable<BaseRespond<List<MapPose>>> mapPosListGet();

    /**
     * 获取到区域列表
     */
    @POST("/robot/api/mapArea/areaListGet")
    Observable<BaseRespond<List<MapArea>>> areaListGet();

    /**
     * 删除点位
     */
    @DELETE("/robot/api/mapPos/deleteById")
    Observable<BaseRespond<Object>> deleteById(@Query("id") int id);

    /**
     * 添加点位
     */
    @POST("/robot/api/mapPos/addMapPos")
    Observable<BaseRespond<MapPose>> addMapPos(@Body PointData pointData);

    /**
     * 点位添加到区域上
     */
    @POST("/robot/api/areaPos/add")
    Observable<BaseRespond<Object>> areaPosAdd(@Body MapAreaData mapAreaData);

    /**
     * 更新区域上点位
     */
    @POST("/robot/api/areaPos/update")
    Observable<BaseRespond<Object>> areaPosUpdate(@Body MapAreaData mapAreaData);

    /**
     * 删除消毒区域
     */
    @DELETE("/robot/api/mapArea/delete")
    Observable<BaseRespond<Object>> mapAreaDelete(@Query("id") int id);

    /**
     * 获取地图区域上的点位
     */
    @POST("/robot/api/areaPos/areaPosList")
    Observable<BaseRespond<List<AreaPose>>> areaPosList(@Body AreaId areaId);
}
