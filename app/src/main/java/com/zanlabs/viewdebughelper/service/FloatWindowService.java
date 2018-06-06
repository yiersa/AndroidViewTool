package com.zanlabs.viewdebughelper.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

import com.zanlabs.viewdebughelper.HomeActivity;
import com.zanlabs.viewdebughelper.MyWindowManager;

import java.util.Timer;
import java.util.TimerTask;

public class FloatWindowService extends Service {

    public static void start(Context context){
        Intent intent=new Intent(context,FloatWindowService.class);
        context.startService(intent);
    }

    public static void stop(Context context){
        Intent intent=new Intent(context,FloatWindowService.class);
        context.stopService(intent);
    }

    private static boolean isRunning=false;

    public static boolean isRunning(){
        return isRunning;
    }
    /**
     * 用于在线程中创建或移除悬浮窗。
     */
   private MyHandler handler;
    class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(!isRunning) {
                handler.removeMessages(1);
                return;
            }
            // 当前界面是桌面，且没有悬浮窗显示，则创建悬浮窗。
            if (!MyWindowManager.isWindowShowing()) {
                MyWindowManager.createFloatWindow(getApplicationContext());
            }else{
                MyWindowManager.updateCurrentTopActvity(getApplicationContext());
            }
            handler.sendEmptyMessageDelayed(1, 1000);
        }
    }

    public FloatWindowService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new MyHandler();
        isRunning=true;
        handler.sendEmptyMessage(1);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning=false;
        handler.removeMessages(1);
        MyWindowManager.removeFloatWindow(getApplicationContext());
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not support");
    }
}
