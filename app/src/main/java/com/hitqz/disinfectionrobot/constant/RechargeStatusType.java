package com.hitqz.disinfectionrobot.constant;

import java.util.HashMap;
import java.util.Map;

public class RechargeStatusType {

    //充电状态 0正常速度控制/1启动回充/2未找到红外信号/3收到充电桩握手信号/4充电正常进行中/5充电过程中出现异常
    // 6电池电量充满，充电结束/7充电停止后退/8充电过程中出现拍硬急停或者碰撞开关/9充电极电压检测故障
    //10充电过程调整的距离达到最大值/11充电失败处理中/12充电失败处理完成/13上位机取消充电

    public final static Map<Integer, String> statusMap = new HashMap<Integer, String>() {{
        put(0, "正常速度控制");
        put(1, "启动回充");
        put(2, "未找到红外信号");
        put(3, "收到充电桩握手信号");
        put(4, "充电正常进行中");
        put(5, "充电过程中出现异常");
        put(6, "电池电量充满，充电结束");
        put(7, "充电停止后退");
        put(8, "充电过程中出现拍硬急停或者碰撞开关");
        put(9, "充电极电压检测故障");
        put(10, "充电过程调整的距离达到最大值");
        put(11, "充电失败处理中");
        put(12, "充电失败处理完成");
        put(13, "上位机取消充电");
    }};
}
