package com.hitqz.disinfectionrobot.util;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

// 目前该类pgm格式值针对P5,  P2格式不支持
public class PGM {
    public static final String TAG = PGM.class.getSimpleName();
    int width = 0;
    int height = 0;
    int maxpix = 0;
    String fileType;

    DataInputStream in;

    int[] mPixs;

    public boolean parse(String fname) {
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(fname);
            in = new DataInputStream(fin);
            return doParse(in);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return false;
        }
    }

    public boolean parse(byte[] bytes) {
        ByteArrayInputStream byteIn = null;
        try {
            byteIn = new ByteArrayInputStream(bytes);
            in = new DataInputStream(byteIn);
            return doParse(in);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean doParse(DataInputStream in) {
        boolean success = true;
        try {
            success = readHeader(in);
            readData();
        } catch (IOException e) {
            success = false;
        }

        return success;
    }

    private boolean readHeader(DataInputStream in) throws IOException {
        fileType = getFileType(in);
        if (fileType.isEmpty()) {
            return false;
        }

        char c = readOneByte(in);
        // 过滤注释
        if (c == '#') {
            do {
                c = (char) in.readByte();
            } while ((c != '\n') && (c != '\r'));
            c = readOneByte(in);
        }

        // 读宽度
        int k = 0;
        do {
            k = k * 10 + c - '0';
            c = (char) in.readByte();
        } while (c >= '0' && c <= '9');
        width = k;

        //读出高度
        // 先读取分隔符数据
        c = (char) in.readByte();
        k = 0;
        do {
            k = k * 10 + c - '0';
            c = (char) in.readByte();
        } while (c >= '0' && c <= '9');
        height = k;

        //读出灰度最大值(尚未使用)
        c = readOneByte(in);
        k = 0;
        do {
            k = k * 10 + c - '0';
            c = (char) in.readByte();
        } while (c >= '0' && c <= '9');
        maxpix = k;
        // 当前类只能处理单字节的灰度
        // 灰度值超过255的处理不了
        return maxpix <= 255;
    }

    private char readOneByte(DataInputStream in) {
        char c = '0';
        try {
            c = (char) in.readByte();
            if (c == '\r') {
                in.readByte();
                c = (char) in.readByte();
            } else if (c == '\n') {
                c = (char) in.readByte();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return c;
    }

    private String getFileType(DataInputStream in) {
        StringBuilder builder = new StringBuilder();

        try {
            char[] typeArray = new char[2];
            typeArray[0] = (char) in.readByte();
            typeArray[1] = (char) in.readByte();

            if (typeArray[0] == 'P' && typeArray[1] == '5') {
                builder.append(typeArray);
            }

            return builder.toString();
        } catch (IOException e) {
            return builder.toString();
        }
    }

    // 直接开辟如此大块的空间，在图片非常大的情况下可能会OOM
    private void readData() {
        if (in == null || width == 0 || height == 0) {
            return;
        }

        int picLen = width * height;
        if (mPixs == null) {
            mPixs = new int[picLen];
        }
        try {
            //读入图像灰度数据, 并生成图像序列
            // 安卓下使用readbytes读取效率很差
            byte[] bytes = new byte[picLen];
            in.read(bytes);

            for (int i = 0; i < bytes.length; i++) {
                int b = bytes[i] & 0xff;

                // 此处首字节透明度填0xff，其他3个RGB字节全部填入同一个值才能显示该颜色
                mPixs[i] = (255 << 24) | (b << 16) | (b << 8) | b;
            }
        } catch (IOException e) {
            return;
        } finally {
            if (in != null) {
                try {
                    in.close();
                    in = null;
                } catch (IOException e) {
                }
            }
        }
    }

    public String getfileType() {
        return fileType;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getMaxpix() {
        return maxpix;
    }

    public int[] getPixs() {
        return mPixs;
    }

    public int[] getMirroredPixs() {
        int[] dest = new int[mPixs.length];
        int srcPos = 0;
        int destPos = mPixs.length;
        int w = getWidth();
        int h = getHeight();
        for (int i = 0; i < h; i++) {
            srcPos = h * i;
            destPos -= w;
            System.arraycopy(mPixs, srcPos, dest, destPos, w);
        }

        return dest;
    }
}
