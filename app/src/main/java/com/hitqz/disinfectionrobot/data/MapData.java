package com.hitqz.disinfectionrobot.data;

import android.graphics.Bitmap;
import android.util.Log;

import com.hitqz.disinfectionrobot.util.PGM;
import com.hitqz.disinfectionrobot.util.YmlUtil;

import java.util.ArrayList;
import java.util.Map;

public class MapData {

    public static final String TAG = MapData.class.getSimpleName();
    public Bitmap bitmap;
    public float resolution;
    public float originX;
    public float originY;
    public boolean valid;

    private MapData() {

    }

    public MapData(String pgmPath, String yamlPath) {
        PGM mPgmMap = new PGM();
        mPgmMap.parse(pgmPath);
        int[] pixs = mPgmMap.getPixs();
        int iw = mPgmMap.getWidth();
        int ih = mPgmMap.getHeight();
        Log.d(TAG, "iw:" + iw + " ih:" + ih);
        bitmap = Bitmap.createBitmap(iw, ih, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixs, 0, iw, 0, 0, iw, ih);

        Map<String, Object> map = YmlUtil.parseYaml(yamlPath);
        if (map != null) {
            resolution = ((Double) map.get("resolution")).floatValue();
            ArrayList<Double> origin = (ArrayList<Double>) map.get("origin");
            originX = origin.get(0).floatValue();
            originY = origin.get(1).floatValue();
        }
    }

    public float getXInMap(float rawX) {
        return (rawX - originX) / resolution;
    }

    public float getYInMap(float rawY) {
        return bitmap.getHeight() - (rawY - originY) / resolution;
    }

    public double getRawX(float drawX) {
        return drawX * resolution + originX;
    }

    public double getRawY(float drawY) {
        return (bitmap.getHeight() - drawY) * resolution + originY;
    }
}
