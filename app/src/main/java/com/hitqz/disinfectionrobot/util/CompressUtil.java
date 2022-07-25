package com.hitqz.disinfectionrobot.util;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

/**
 * @author wanghr
 * 创建时间：2018/8/21 14:00
 */
public class CompressUtil {
    private static final String TAG = "CompressUtil";

    public static byte[] compress(byte[] data) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DeflaterOutputStream dos = new DeflaterOutputStream(baos);
        try {
            dos.write(data);
            dos.flush();
            dos.close();
            return baos.toByteArray();
        } catch (IOException e) {
            Log.w(TAG, "do compress error", e);
            return null;
        }
    }

    public static byte[] decompress(byte[] data) {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        InflaterInputStream iis = new InflaterInputStream(bais);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int rlen = -1;
        byte[] buf = new byte[128];

        try {
            while ((rlen = iis.read(buf)) != -1) {
                out.write(Arrays.copyOf(buf, rlen));
            }
            return out.toByteArray();
        } catch (IOException e) {
            Log.w(TAG, "decompress error", e);
            return null;
        }
    }
}
