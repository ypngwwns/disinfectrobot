package com.hitqz.disinfectionrobot.data;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PointF;
import android.util.Log;

import java.util.Arrays;

public class BuildMap {
    public static final String TAG = BuildMap.class.getSimpleName();
    private float mResolution = 1.0f;
    private final PointF mMapOrigin = new PointF(0.0f, 0.0f);
    private final PointF mImageShowOrigin = new PointF(0.0f, 0.0f);
    private final float mShowScale = 1.0f;
    private Bitmap mMapBitmap = null;
    private int mMapWidth = 0;
    private int mMapHeight = 0;
    private int[] mUpdateInts = new int[1];

    public PointF getDrawPoint(PointF p) {
        Log.d(TAG, "getDrawPoint mResolution===" + mResolution);
        Log.d(TAG, "getDrawPoint mShowScale===" + mShowScale);
        PointF res = new PointF(
                (((p.x - mMapOrigin.x) / mResolution) + mImageShowOrigin.x) * mShowScale,
                (((p.y - mMapOrigin.y) / mResolution) + mImageShowOrigin.y) * mShowScale
        );
        return res;
    }

    public void reloadMap(OccupancyGrid map) {

        PointF origin = new PointF((float) map.getOrigin().getPosition().getX(), (float) map.getOrigin().getPosition().getY());
        int width = map.getWidth();
        int height = map.getHeight();
        float resolution = map.getResolution();

//        Log.d(TAG, "resolution===" + resolution);
//        Log.d(TAG, "origin.x===" + origin.x + "\norigin.y===" + origin.y +
//                "\nwidth===" + width + "\nheight===" + height);
        if (mMapBitmap == null) {// 第一张图
            Log.d(TAG, "初始origin.x===" + origin.x + "\n初始origin.y===" + origin.y +
                    "\n初始width===" + width + "\n初始height===" + height);
            mMapBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            mMapBitmap.setPixels(initPixs(width, height), 0, width, 0, 0, width, height);

            updateMap(map, origin, width, height, resolution);
        } else {// 更新地图
            if (mMapWidth == width && mMapHeight == height) {
                //  宽高不变只更新像素数据
                updateMap(map, origin, width, height, resolution);
            } else {// 宽高变化，需要以新宽高为准，拷贝相交部分像素数据
//                Log.d(TAG, "变化width===" + width + "\n变化height===" + height + "\n变化origin.x===" + origin.x +
//                        "\n变化origin.y===" + origin.y);
                Bitmap dstbitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                int[] beforeInts = new int[mMapWidth * mMapHeight];
                mMapBitmap.getPixels(beforeInts, 0, mMapWidth, 0, 0, mMapWidth, mMapHeight);
                int x1 = (int) Math.max(mMapOrigin.x / mResolution, origin.x / resolution);
                int x2 = (int) Math.min(mMapOrigin.x / mResolution + mMapWidth, origin.x / resolution + width);
                int y1 = (int) Math.max(mMapOrigin.y / mResolution, origin.y / resolution);
                int y2 = (int) Math.min(mMapOrigin.y / mResolution + mMapHeight, origin.y / resolution + height);
                int dx = Math.abs(x1 - x2);
                int dy = Math.abs(y1 - y2);
//                Log.d(TAG, "x1:" + x1 + " x2:" + x2 + " y1:" + y1 + " y2:" + y2);
//                Log.d(TAG, "dx:" + dx + " dy:" + dy);

                int[] dest = initPixs(width, height);
                int srcPos = 0;
                int destPos = 0;

                for (int i = 0; i < dy; i++) {
                    srcPos = (int) (x1 - mMapOrigin.x / mResolution + (i + Math.abs(y1 - mMapOrigin.y / mResolution)) * mMapWidth);
                    destPos = (int) (x1 - origin.x / mResolution + (i + Math.abs(y1 - origin.y / resolution)) * width);
                    System.arraycopy(beforeInts, srcPos, dest, destPos, dx);
                }

                dstbitmap.setPixels(dest, 0, width, 0, 0, width, height);

                mMapBitmap = dstbitmap;

                updateMap(map, origin, width, height, resolution);
            }
        }
    }

    private void updateMap(OccupancyGrid map, PointF origin, int width, int height, float resolution) {
        byte[] data = map.getData().toByteArray();
        Log.d(TAG, "更新坐标的数量:" + data.length);
        for (int i = 0; i < data.length; i++) {
            int index = map.getIndicesList().get(i);
            if (mUpdateInts.length < data.length) {
                mUpdateInts = new int[data.length];
            }
            int b = data[i];
            if (b == -1) {
                b = 205;
            } else {
                b = (int) ((100 - data[i]) / 100f * 255);
            }
            mUpdateInts[i] = Color.argb(255, b, b, b);
            mMapBitmap.setPixel(index % width, index / width, mUpdateInts[i]);
        }
        mMapWidth = width;
        mMapHeight = height;
        mMapOrigin.x = origin.x;
        mMapOrigin.y = origin.y;
        mResolution = resolution;
    }

    private int[] initPixs(int w, int h) {
        int[] pixs = new int[w * h];//像素数量

        // 此处首字节透明度填0xff，其他3个RGB字节全部填入同一个值才能显示该颜色
        Arrays.fill(pixs, 0xffcdcdcd);

        return pixs;
    }

    public void setImageShowOrigin(int vw, int vh) {
        if (mMapBitmap != null) {
            this.mImageShowOrigin.x = vw / 2.0f - (mMapBitmap.getWidth() * mShowScale) / 2f;
            this.mImageShowOrigin.y = vh / 2.0f - (mMapBitmap.getHeight() * mShowScale) / 2f;
        }
    }

    public PointF getImageShowOrigin() {
        return mImageShowOrigin;
    }

    public Bitmap getBitMap() {
        return mMapBitmap;
    }

    public float getShowScale() {
        return mShowScale;
    }

    public int getmMapWidth() {
        return mMapWidth;
    }

    public int getmMapheight() {
        return mMapHeight;
    }
}
