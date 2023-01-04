package com.hitqz.disinfectionrobot;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;

import com.hitqz.disinfectionrobot.net.ws.JWebSocketClientService;

import org.litepal.LitePal;

public class DisinfectRobotApplication extends Application implements ServiceConnection {
    public static DisinfectRobotApplication instance;
    public JWebSocketClientService jWebSClientService;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        LitePal.initialize(this);
        startWebSocketService();
        bindService();
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        JWebSocketClientService.JWebSocketClientBinder binder = (JWebSocketClientService.JWebSocketClientBinder) service;
        jWebSClientService = binder.getService();

    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    private void startWebSocketService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(this, JWebSocketClientService.class));
        } else {
            startService(new Intent(this, JWebSocketClientService.class));
        }
    }

    private void bindService() {
        Intent bindIntent = new Intent(this, JWebSocketClientService.class);
        bindService(bindIntent, this, BIND_AUTO_CREATE);
    }
}
