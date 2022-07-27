package com.hitqz.disinfectionrobot.net;

import com.hitqz.disinfectionrobot.data.LoginRequest;
import com.hitqz.disinfectionrobot.data.LoginResponse;
import com.hitqz.disinfectionrobot.data.MapAreaSetRequest;
import com.hitqz.disinfectionrobot.data.MapChangeRequest;
import com.hitqz.disinfectionrobot.data.MapDataGetResponse;
import com.hitqz.disinfectionrobot.data.MapListGetRequest;
import com.hitqz.disinfectionrobot.data.MapListGetResponse;
import com.hitqz.disinfectionrobot.data.MapPosAddRequest;
import com.hitqz.disinfectionrobot.data.MapPosAddResponse;
import com.hitqz.disinfectionrobot.data.MapPosDeleteRequest;
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
    Observable<BaseRespond<LoginResponse>> map_build(@Body LoginRequest request);

    /**
     * 读取地图列表
     */
    @POST("/robot/api/map/map_list_get")
    Observable<BaseRespond<MapListGetResponse>> map_list_get(@Body MapListGetRequest request);

    /**
     * 读取地图数据
     */
    @POST("/robot/api/map/map_data_get")
    Observable<BaseRespond<MapDataGetResponse>> map_data_get(@Body MapListGetRequest request);

    /**
     * 读取地图节点
     */
    @POST("/robot/api/map/map_pos_get")
    Observable<BaseRespond<MapDataGetResponse>> map_pos_get(@Body MapListGetRequest request);

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
}
