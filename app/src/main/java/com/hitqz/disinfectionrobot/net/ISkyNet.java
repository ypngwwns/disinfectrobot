package com.hitqz.disinfectionrobot.net;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ISkyNet {

    /**
     * 获取实兵演习百度地图信息
     *
     * @return MapBean
     */
    @GET("/app/map")
    Observable<BaseRespond<MapBean>> map();

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
