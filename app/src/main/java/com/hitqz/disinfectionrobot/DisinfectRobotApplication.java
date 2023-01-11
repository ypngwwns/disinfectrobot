package com.hitqz.disinfectionrobot;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.blankj.utilcode.util.ToastUtils;
import com.hitqz.disinfectionrobot.log.CrashUtil;
import com.hitqz.disinfectionrobot.net.ws.JWebSocketClientService;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class DisinfectRobotApplication extends Application implements ServiceConnection {
    public static final String TAG = DisinfectRobotApplication.class.getSimpleName();
    public static DisinfectRobotApplication instance;
    public JWebSocketClientService jWebSClientService;
    private List<JWebSocketClientService.WebSocketCallback> mWebSocketCallbacks = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        LitePal.initialize(this);
        CrashUtil.getInstance().init(getApplicationContext(), "DisinfectRobot");
        startWebSocketService();
        bindService();
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        JWebSocketClientService.JWebSocketClientBinder binder = (JWebSocketClientService.JWebSocketClientBinder) service;
        jWebSClientService = binder.getService();
        for (JWebSocketClientService.WebSocketCallback webSocketCallback : mWebSocketCallbacks
        ) {
            if (!jWebSClientService.mWebSocketCallbacks.contains(webSocketCallback)) {
                Log.e(TAG, "JWebSocketClient jWebSClientService.mWebSocketCallbacks.add(webSocketCallback);");
                jWebSClientService.mWebSocketCallbacks.add(webSocketCallback);
            }
        }
        Log.e(TAG, "JWebSocketClient.mWebSocketCallbacks.size:" + jWebSClientService.mWebSocketCallbacks.size());
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

    public void addWebSocketCallback(JWebSocketClientService.WebSocketCallback webSocketMessageReceiver) {
        if (jWebSClientService != null) {
            Log.e(TAG, "JWebSocketClient jWebSClientService != null");
            jWebSClientService.mWebSocketCallbacks.add(webSocketMessageReceiver);
            if (jWebSClientService.client != null && jWebSClientService.client.isOpen()) {
                Log.e(TAG, "JWebSocketClient jWebSClientService onConnectSuccess");
                webSocketMessageReceiver.onConnectSuccess(jWebSClientService.client.getURI().toString());
            }
        } else {
            Log.e(TAG, "JWebSocketClient jWebSClientService == null");
            mWebSocketCallbacks.add(webSocketMessageReceiver);
        }
    }

    public void removeWebSocketCallback(JWebSocketClientService.WebSocketCallback webSocketMessageReceiver) {
        if (jWebSClientService != null) {
            jWebSClientService.mWebSocketCallbacks.remove(webSocketMessageReceiver);
        } else {
            ToastUtils.showShort("jWebSClientService为空！！");
        }
    }
}
