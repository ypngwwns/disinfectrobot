package com.hitqz.disinfectionrobot.net;

import com.hitqz.disinfectionrobot.data.AreaId;
import com.hitqz.disinfectionrobot.data.AreaPose;
import com.hitqz.disinfectionrobot.data.Cmd;
import com.hitqz.disinfectionrobot.data.DisinfectTask;
import com.hitqz.disinfectionrobot.data.LoginRequest;
import com.hitqz.disinfectionrobot.data.LoginResponse;
import com.hitqz.disinfectionrobot.data.MapArea;
import com.hitqz.disinfectionrobot.data.MapAreaData;
import com.hitqz.disinfectionrobot.data.MapCode;
import com.hitqz.disinfectionrobot.data.MapDataResponse;
import com.hitqz.disinfectionrobot.data.MapListData;
import com.hitqz.disinfectionrobot.data.MapPose;
import com.hitqz.disinfectionrobot.data.PointData;
import com.hitqz.disinfectionrobot.data.SpeedRequest;
import com.hitqz.disinfectionrobot.data.Task;
import com.hitqz.disinfectionrobot.data.TempTask;
import com.hitqz.disinfectionrobot.net.data.CleanTask;

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

    /**
     * 获取机器人地图列表
     */
    @POST("/robot/api/ctrl/mapListGet")
    Observable<BaseRespond<MapListData>> mapListGet();

    /**
     * 切换地图
     */
    @POST("/robot/api/ctrl/navSwitchMap")
    Observable<BaseRespond<Object>> navSwitchMap(@Body MapCode mapCode);

    /**
     * 获取到任务列表
     */
    @POST("/robot/api/taskListGet")
    Observable<BaseRespond<List<Task>>> taskListGet();

    /**
     * 添加任务
     */
    @POST("/robot/api/add")
    Observable<BaseRespond<Object>> addTask(@Body DisinfectTask disinfectTask);

    /**
     * 更新任务
     */
    @POST("/robot/api/update")
    Observable<BaseRespond<Object>> updateTask(@Body DisinfectTask disinfectTask);

    /**
     * 删除任务
     */
    @DELETE("/robot/api/delete")
    Observable<BaseRespond<Object>> deleteTask(@Query("id") int id);

    /**
     * 建图指令
     */
    @POST("/robot/api/ctrl/buildMap")
    Observable<BaseRespond<Object>> buildMap();

    /**
     * 结束建图
     */
    @POST("/robot/api/ctrl/finishBuildMap")
    Observable<BaseRespond<Object>> finishBuildMap(@Body MapCode mapCode);

    /**
     * 结束建图
     */
    @POST("/robot/api/ctrl/cancelBuildMap")
    Observable<BaseRespond<Object>> cancelBuildMap();

    /**
     * 修改任务状态
     */
    @POST("/robot/api/activeJob")
    Observable<BaseRespond<Object>> activeJob(@Body Task task);

    /**
     * 添加任务，立即执行
     */
    @POST("/robot/api/addJobNow")
    Observable<BaseRespond<Object>> addJobNow(@Body TempTask task);

    /**
     * 获取清洁任务列表
     */
    @POST("/robot/api/clean/taskListGet")
    Observable<BaseRespond<List<CleanTask>>> cleanTaskListGet();
}
