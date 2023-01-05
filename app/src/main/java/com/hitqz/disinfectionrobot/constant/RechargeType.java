package com.hitqz.disinfectionrobot.constant;

import java.util.HashMap;
import java.util.Map;

public class RechargeType {

    /**
     * 定位模式 0-AMCL 1-Cartographer, 2-ndt
     */
    public final static Map<Integer, String> statusMap = new HashMap<Integer, String>() {{
        put(0, "AMCL");
        put(1, "Cartographer");
        put(2, "ndt");
    }};
}
