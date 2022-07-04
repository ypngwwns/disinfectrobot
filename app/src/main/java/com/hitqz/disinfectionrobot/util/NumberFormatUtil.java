package com.hitqz.disinfectionrobot.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumberFormatUtil {
    public static String format(double value) {

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.toString();
    }
}
