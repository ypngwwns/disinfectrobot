package com.hitqz.disinfectionrobot.data;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SaveUtil {
    // 将Bitmap保存到设备存储的方法
    public static String saveBitmapToStorage(Context context, Bitmap bitmap, String fileName) {
        File directory = context.getApplicationContext().getDir("images", Context.MODE_PRIVATE);
        File file = new File(directory, fileName + ".png");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
