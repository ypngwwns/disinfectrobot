package com.hitqz.disinfectionrobot.util;

import com.hitqz.disinfectionrobot.data.NavigationPoint;

import org.litepal.LitePal;

import java.util.List;

public class DBHelper {

    public static List<NavigationPoint> findAllNavigationPoint(String mapCode) {
        return LitePal.where("mapCode = ? and name != ?", mapCode, "recharge_pos").find(NavigationPoint.class);
    }

    public static NavigationPoint findRechargePos(String mapCode) {
        List<NavigationPoint> rechargePoss =
                LitePal.where("mapCode = ? and name = ?", mapCode, "recharge_pos").find(NavigationPoint.class);
        if (rechargePoss != null && rechargePoss.size() > 0) {
            return rechargePoss.get(0);
        }
        return null;
    }
}
