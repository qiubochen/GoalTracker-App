package com.example.qiubo.goaltracker.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.example.qiubo.goaltracker.model.DO.Event;
import com.example.qiubo.goaltracker.view.impl.LockScreenActivity;

public class LockService extends Service {
    public LockService() {
    }
    BroadcastReceiver receiver;
    @Override
    public void onCreate() {
        //super.onCreate();
       receiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction() == Intent.ACTION_SCREEN_OFF) {

                    System.out.println("收到锁屏广播");
                    Intent lockscreen = new Intent(LockService.this, LockScreenActivity.class);
                    lockscreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(lockscreen);
                }
            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(receiver, filter);
    }


    @Override
    public void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
