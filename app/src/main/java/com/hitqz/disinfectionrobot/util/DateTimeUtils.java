package com.hitqz.disinfectionrobot.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 与时间，日期相关的帮助类
 */
public class DateTimeUtils {
    /**
     * 日期时间分隔符key
     */
    public static final String DATE_SEPARATOR = "-";
    public static final String TIME_SEPARATOR = ":";
    /**
     * 时间格式
     */
    public static final String TIME_FORMAT = "HH:mm:ss";
    /**
     * 日期格式
     */
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    /**
     * 日期时间格式
     */
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 时间转换成字符串
     *
     * @param date
     * @return str
     */
    public static String TimeToStr(Date date) {

        SimpleDateFormat format = new SimpleDateFormat(TIME_FORMAT, Locale.CHINA);
        String str = format.format(date);
        return str;
    }

    /**
     * 字符串转换成时间
     *
     * @param str
     * @return date
     */
    public static Date StrToTime(String str) {

        SimpleDateFormat format = new SimpleDateFormat(TIME_FORMAT, Locale.CHINA);
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 日期转换成字符串
     *
     * @param date
     * @return str
     */
    public static String DateToStr(Date date) {

        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT, Locale.CHINA);
        String str = format.format(date);
        return str;
    }

    /**
     * 字符串转换成日期
     *
     * @param str
     * @return date
     */
    public static Date StrToDate(String str) {

        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT, Locale.CHINA);
        Date date = null;
        try {
            date = format.parse(str);
            date.setHours(12); //防止错误
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获取n天前的日期 日期-n天前格式化 yyyy-MM-dd
     *
     * @param n
     * @return
     */
    public static String DateCutNdateFormat(int n) {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        Date curDate = new Date(System.currentTimeMillis() - n * 24 * 60 * 60 * 1000L);// 获取当前时间 n天前
        String str = formatter.format(curDate);
        return str;
    }

    /**
     * 日期时间转换成字符串
     *
     * @param date
     * @return str
     */
    public static String DateTimeToStr(Date date) {

        SimpleDateFormat format = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.CHINA);
        String str = format.format(date);
        return str;
    }

    /**
     * 字符串转换成日期时间
     *
     * @param str
     * @return date
     */
    public static Date StrToDateTime(String str) {

        SimpleDateFormat format = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.CHINA);
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    /**
     * 获取年龄
     *
     * @param birthDay
     * @return
     */
    public static int getAge(Calendar birthDay) {
        Calendar cal = Calendar.getInstance();
        if (cal.before(birthDay)) {
            return 0;
        }
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

        int yearBirth = birthDay.get(Calendar.YEAR);
        int monthBirth = birthDay.get(Calendar.MONTH);
        int dayOfMonthBirth = birthDay.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;
        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth)
                    age--;
            } else {
                age--;
            }
        }
        return age;
    }

    /**
     * 获取年龄
     *
     * @param date
     * @return
     */
    public static int getAge(Date date) {
        Calendar birthDay = Calendar.getInstance();
        birthDay.setTime(date);
        return getAge(birthDay);
    }

    /**
     * 获取两个日期之间的间隔天数
     * @param startDate
     * @param endDate
     * @return
     */
    public static int getGapCount(Date startDate, Date endDate) {
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(startDate);
        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
        fromCalendar.set(Calendar.MINUTE, 0);
        fromCalendar.set(Calendar.SECOND, 0);
        fromCalendar.set(Calendar.MILLISECOND, 0);
        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(endDate);
        toCalendar.set(Calendar.HOUR_OF_DAY, 0);
        toCalendar.set(Calendar.MINUTE, 0);
        toCalendar.set(Calendar.SECOND, 0);
        toCalendar.set(Calendar.MILLISECOND, 0);
        return (int) ((toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24));
    }

}
