package com.example.qiubo.goaltracker.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.widget.Toast;

import com.example.qiubo.goaltracker.view.impl.LockScreenActivity;

import java.util.Date;

public class HomeReceiver extends BroadcastReceiver {

    static public final String SYSTEM_DIALOG_REASON_KEY = "reason";
    static public final String SYSTEM_DIALOG_REASON_GLOBAL_ACTIONS = "globalactions";
    static public final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
    static public final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
    static public final String SYSTEM_DIALOG_REASON_ASSIST = "assist";


    @Override
    public  void onReceive(Context context, Intent intent1) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.


        String action = intent1.getAction();
        if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {                                           //按下Home键会发送ACTION_CLOSE_SYSTEM_DIALOGS的广播
            System.out.println("5555555");
            String reason = intent1.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
            if (reason != null) {
                if (reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)||reason.equals(SYSTEM_DIALOG_REASON_RECENT_APPS)) {
                    //synchronized (this) {
                        Intent intent = new Intent(context, LockScreenActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION
                                | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);       //点击home键无延时，且不会产生新的activity
                        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

                        try {
                            System.out.println("111111");
//                            for (int i=1;i<5;i++) {
//                                pendingIntent.send();
//                                Thread.sleep(200L);
//                            }
                            Thread.sleep(1000L);
                            pendingIntent.send();
                            Thread.sleep(4900L);
                            pendingIntent.send();
                           // Toast.makeText(context, "成功", Toast.LENGTH_SHORT).show();
                            System.out.println("222222");
                            //Thread.sleep(5000L);
                        } catch (PendingIntent.CanceledException e) {
                            Toast.makeText(context, "不成功", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    // }
                }
            }
        }


    }
}
