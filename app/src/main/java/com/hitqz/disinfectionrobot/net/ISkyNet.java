package com.hitqz.disinfectionrobot.net;

import com.hitqz.disinfectionrobot.data.Cmd;
import com.hitqz.disinfectionrobot.data.LoginRequest;
import com.hitqz.disinfectionrobot.data.LoginResponse;
import com.hitqz.disinfectionrobot.data.MapAreaListResponse;
import com.hitqz.disinfectionrobot.data.MapAreaPosResponse;
import com.hitqz.disinfectionrobot.data.MapAreaRequest;
import com.hitqz.disinfectionrobot.data.MapAreaSetRequest;
import com.hitqz.disinfectionrobot.data.MapAreaUpdateRequest;
import com.hitqz.disinfectionrobot.data.MapBuildRequest;
import com.hitqz.disinfectionrobot.data.MapChangeRequest;
import com.hitqz.disinfectionrobot.data.MapDataGetRequest;
import com.hitqz.disinfectionrobot.data.MapDataGetResponse;
import com.hitqz.disinfectionrobot.data.MapListGetRequest;
import com.hitqz.disinfectionrobot.data.MapListGetResponse;
import com.hitqz.disinfectionrobot.data.MapPosAddRequest;
import com.hitqz.disinfectionrobot.data.MapPosAddResponse;
import com.hitqz.disinfectionrobot.data.MapPosDeleteRequest;
import com.hitqz.disinfectionrobot.data.MapPosGetRequest;
import com.hitqz.disinfectionrobot.data.MapPosGetResponse;
import com.hitqz.disinfectionrobot.data.MapUploadRequest;
import com.hitqz.disinfectionrobot.data.ScheduleAddRequest;
import com.hitqz.disinfectionrobot.data.ScheduleListResponse;
import com.hitqz.disinfectionrobot.data.ScheduleRequest;
import com.hitqz.disinfectionrobot.data.ScheduleUpdateRequest;
import com.hitqz.disinfectionrobot.data.SpeedRequest;
import com.hitqz.disinfectionrobot.data.Token;
import com.hitqz.disinfectionrobot.data.UrgentTaskRequest;
import com.hitqz.disinfectionrobot.net.data.UserLoginData;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ISkyNet {

    /**
     * Oauth2获取token
     */
    @FormUrlEncoded
    @POST("/robot-auth/oauth/token")
    Observable<BaseRespond<UserLoginData>> oauthToken(@Field("client_id") String client_id,
                                                      @Field("client_secret") String client_secret,
                                                      @Field("grant_type") String grant_type,
                                                      @Field("username") String username,
                                                      @Field("password") String password,
                                                      @Field("refresh_token") String refresh_token);

    /**
     * 登录
     *
     * @return List<VestBean>
     */
    @POST("/robot/api/login")
    Observable<BaseRespond<LoginResponse>> login(@Body LoginRequest request);

    /**
     * 建图
     * <p>
     * 0:开始建图
     * 1:停止建图
     */
    @POST("/robot/api/map/map_build")
    Observable<BaseRespond<Object>> map_build(@Body MapBuildRequest request);

    /**
     * 地图保存
     */
    @POST("/robot/api/map/map_upload")
    Observable<BaseRespond<Object>> map_upload(@Body MapUploadRequest request);

    /**
     * 读取地图列表
     */
    @POST("/robot/api/map/map_list_get")
    Observable<BaseRespond<MapListGetResponse>> map_list_get(@Body MapListGetRequest request);

    /**
     * 读取地图数据
     */
    @POST("/robot/api/map/map_data_get")
    Observable<BaseRespond<MapDataGetResponse>> map_data_get(@Body MapDataGetRequest request);

    /**
     * 读取地图节点
     */
    @POST("/robot/api/map/map_pos_get")
    Observable<BaseRespond<MapPosGetResponse>> map_pos_get(@Body MapPosGetRequest request);

    /**
     * 切换地图
     */
    @POST("/robot/api/map/map_change")
    Observable<BaseRespond<Object>> map_change(@Body MapChangeRequest request);

    /**
     * 新增节点
     */
    @POST("/robot/api/map/map_pos_add")
    Observable<BaseRespond<MapPosAddResponse>> map_pos_add(@Body MapPosAddRequest request);

    /**
     * 新增节点
     */
    @POST("/robot/api/map/map_pos_delete")
    Observable<BaseRespond<Object>> map_pos_delete(@Body MapPosDeleteRequest request);

    /**
     * 新建消毒区
     */
    @POST("/robot/api/map/map_area_set")
    Observable<BaseRespond<Object>> map_area_set(@Body MapAreaSetRequest request);

    /**
     * 消毒区列表读取
     */
    @POST("/robot/api/map/map_area_list_get")
    Observable<BaseRespond<MapAreaListResponse>> map_area_list_get(@Body Token token);

    /**
     * 消毒区节点读取
     */
    @POST("/robot/api/map/map_area_pos_get")
    Observable<BaseRespond<MapAreaPosResponse>> map_area_pos_get(@Body MapAreaRequest request);

    /**
     * 消毒区更新
     */
    @POST("/robot/api/map/map_area_update")
    Observable<BaseRespond<Object>> map_area_update(@Body MapAreaUpdateRequest request);

    /**
     * 消毒区删除
     */
    @POST("/robot/api/map/map_area_delete")
    Observable<BaseRespond<Object>> map_area_delete(@Body MapAreaRequest request);

    /**
     * 定时任务列表读取
     */
    @POST("/robot/api/task/schedule_read")
    Observable<BaseRespond<ScheduleListResponse>> schedule_read(@Body ScheduleRequest request);

    /**
     * 定时任务 - 新增
     */
    @POST("/robot/api/task/schedule_add")
    Observable<BaseRespond<Object>> schedule_add(@Body ScheduleAddRequest request);

    /**
     * 定时任务 - 修改
     */
    @POST("/robot/api/task/schedule_update")
    Observable<BaseRespond<Object>> schedule_update(@Body ScheduleUpdateRequest request);

    /**
     * 定时任务 - 删除
     */
    @POST("/robot/api/task/schedule_delete")
    Observable<BaseRespond<Object>> schedule_delete(@Body ScheduleRequest request);

    /**
     * 临时任务 - 下达
     */
    @POST("/robot/api/task/urgent_task")
    Observable<BaseRespond<Object>> urgent_task(@Body UrgentTaskRequest request);

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
}
