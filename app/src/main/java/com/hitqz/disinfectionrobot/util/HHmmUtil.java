package com.hitqz.disinfectionrobot.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HHmmUtil {
    public static final SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("HH:mm");

    public static String format(String time) {
        try {
            Date date = mSimpleDateFormat.parse(time);
            if (date != null) {
                return mSimpleDateFormat.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
}
