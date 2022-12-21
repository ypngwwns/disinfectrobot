package com.hitqz.disinfectionrobot.singleton;

import android.content.Context;

import com.hitqz.disinfectionrobot.net.ISkyNet;
import com.hitqz.disinfectionrobot.net.RetrofitManager;

public class ChassisManager {
    private static volatile ChassisManager mSingleton;
    private Context mContext;
    private ISkyNet mISkyNet;

    private ChassisManager(Context context) {
        mContext = context.getApplicationContext();
        mISkyNet = RetrofitManager.getInstance(mContext).create(ISkyNet.class);
    }

    public static ChassisManager getInstance(Context context) {
        if (mSingleton == null) {
            synchronized (ChassisManager.class) {
                if (mSingleton == null) {
                    mSingleton = new ChassisManager(context);
                }
            }
        }

        return mSingleton;
    }
}
