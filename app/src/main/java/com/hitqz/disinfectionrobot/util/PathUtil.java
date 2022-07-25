package com.hitqz.disinfectionrobot.util;

import android.content.Context;
import android.os.Build;
import android.os.Environment;

import java.io.File;

public class PathUtil {

    public static String getMapCompressFilePath(Context context, String mapName) {
        return getSDPath(context) + "/" + mapName + ".tar.gz";
    }

    public static File getMapDeCompressFile(Context context, String mapName) {
        File file = new File(getSDPath(context) + "/" + mapName);
        if (file.exists()) {
            file.delete();
        }
        file.mkdirs();

        return file;
    }

    public static String getMapPGMFile(Context context, String mapName) {
        return getSDPath(context) + "/" + mapName + "/" + mapName + ".pgm";
    }

    public static String getMapYmlFile(Context context, String mapName) {
        return getSDPath(context) + "/" + mapName + "/" + mapName + ".yaml";
    }

    public static String getSDPath(Context context) {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);// 判断sd卡是否存在
        if (sdCardExist) {
            if (Build.VERSION.SDK_INT >= 29) {
                //Android10之后
                sdDir = context.getExternalFilesDir(null);
            } else {
                sdDir = Environment.getExternalStorageDirectory();// 获取SD卡根目录
            }
        } else {
            sdDir = Environment.getRootDirectory();// 获取跟目录
        }
        return sdDir.toString();
    }
}
