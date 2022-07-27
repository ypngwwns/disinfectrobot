package com.hitqz.disinfectionrobot.net;

import com.hitqz.disinfectionrobot.data.Goal;
import com.hitqz.disinfectionrobot.data.LoginRequest;
import com.hitqz.disinfectionrobot.data.LoginResponse;
import com.hitqz.disinfectionrobot.data.MapDataGetResponse;
import com.hitqz.disinfectionrobot.data.MapListGetRequest;
import com.hitqz.disinfectionrobot.data.MapListGetResponse;
import com.hitqz.disinfectionrobot.data.MapPos;
import com.hitqz.disinfectionrobot.net.data.UserLoginData;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

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
     * 获取马甲的信息
     *
     * @return VestBean
     */
    @GET("/robot-web/web/robotMapPos/getByMapCheck")
    Observable<BaseRespond<List<Goal>>> getByMapCheck(@Query("mapCode") String mapCode);

    /**
     * 获取马甲同一阵营的信息
     *
     * @return List<VestBean>
     */
    @GET("/app/teamInfo/{num}")
    Observable<BaseRespond<List<VestBean>>> teamInfo(@Path("num") int num);

    /**
     * 获取演习日志
     *
     * @return BattleInfoBean
     */
    @GET("/app/battleInfo/{num}")
    Observable<BaseRespond<List<BattleInfoBean>>> battleInfo(@Path("num") int num);

    /**
     * 获取演习日志
     *
     * @return List<VestBean>
     */
    @GET("/app/battleStatus")
    Observable<BaseRespond<Integer>> battleStatus();

    /**
     * 新增节点
     *
     * @return List<VestBean>
     */
    @POST("/robot/api/map/map_pos_add")
    Observable<BaseRespond<Integer>> map_pos_add(@Body MapPos mapPos);

    /**
     * 登录
     *
     * @return List<VestBean>
     */
    @POST("/robot/api/login")
    Observable<BaseRespond<LoginResponse>> login(@Body LoginRequest loginRequest);

    /**
     * 建图
     * <p>
     * 0:开始建图
     * 1:停止建图
     */
    @POST("/robot/api/map/map_build")
    Observable<BaseRespond<LoginResponse>> map_build(@Body LoginRequest loginRequest);

    /**
     * 读取地图列表
     */
    @POST("/robot/api/map/map_list_get")
    Observable<BaseRespond<MapListGetResponse>> map_list_get(@Body MapListGetRequest mapListGetRequest);

    /**
     * 读取地图数据
     */
    @POST("/robot/api/map/map_data_get")
    Observable<BaseRespond<MapDataGetResponse>> map_data_get(@Body MapListGetRequest mapListGetRequest);
}
