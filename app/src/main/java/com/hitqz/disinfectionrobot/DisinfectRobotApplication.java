package com.hitqz.disinfectionrobot;

import android.app.Application;
import android.content.Context;

public class DisinfectRobotApplication extends Application {
    public static Context instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
