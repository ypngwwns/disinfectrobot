package com.hitqz.disinfectionrobot.util;

import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil {

    public static final String TAG = FileUtil.class.getSimpleName();

    public static boolean save2Path(byte[] data, String fileName) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(fileName);
            fos.write(data);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return false;
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                Log.e(TAG, e.toString());
            }
        }
        return true;
    }
}
