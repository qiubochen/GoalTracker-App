package com.example.qiubo.goaltracker.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.widget.Toast;

import com.example.qiubo.goaltracker.view.impl.LockScreenActivity;

public class HomeService extends Service {
    public HomeService() {
    }
    BroadcastReceiver receiver;
    static public final String SYSTEM_DIALOG_REASON_KEY = "reason";
    static public final String SYSTEM_DIALOG_REASON_GLOBAL_ACTIONS = "globalactions";
    static public final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
    static public final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
    static public final String SYSTEM_DIALOG_REASON_ASSIST = "assist";

    @Override
    public void onCreate() {
        //super.onCreate();
        receiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent1) {

                String action = intent1.getAction();
                if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {                                           //按下Home键会发送ACTION_CLOSE_SYSTEM_DIALOGS的广播
                    String reason = intent1.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
                    if (reason != null) {
                        if (reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {
                            Intent intent = new Intent(context, LockScreenActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);       //点击home键无延时，且不会产生新的activity
                            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                                    intent, 0);
                            try {
                                pendingIntent.send();
                                Toast.makeText(context,"成功",Toast.LENGTH_SHORT).show();
                            } catch (PendingIntent.CanceledException e) {
                                Toast.makeText(context,"不成功",Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    }
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
