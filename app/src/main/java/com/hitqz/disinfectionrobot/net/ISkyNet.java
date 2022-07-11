package com.hitqz.disinfectionrobot.net;

import com.hitqz.disinfectionrobot.net.data.UserLoginData;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

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
    @GET("/app/vest/{num}")
    Observable<BaseRespond<VestBean>> vest(@Path("num") int num);

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
}
