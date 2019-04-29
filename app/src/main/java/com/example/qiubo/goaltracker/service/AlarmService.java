package com.example.qiubo.goaltracker.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;

import com.example.qiubo.goaltracker.receiver.AlarmReceiver;
import com.example.qiubo.goaltracker.util.AlarmTimer;
import com.prolificinteractive.materialcalendarview.CalendarDay;

public class AlarmService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

       // AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        //读者可以修改此处的Minutes从而改变提醒间隔时间
        //此处是设置每隔90分钟启动一次
        //这是90分钟的毫秒数
        int Minutes = 1*20*1000;
        //SystemClock.elapsedRealtime()表示1970年1月1日0点至今所经历的时间
        long triggerAtTim = SystemClock.elapsedRealtime() + Minutes;
        System.out.println(triggerAtTim);
        long triggerAtTime=intent.getLongExtra("time",0L);
//        //此处设置开启AlarmReceiver这个Service
//        Intent i = new Intent(this, AlarmReceiver.class);
//        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
//        //ELAPSED_REALTIME_WAKEUP表示让定时任务的出发时间从系统开机算起，并且会唤醒CPU。
//        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
//
//        System.out.println("AlarmService看看有没有");
//        //AlarmTimer.setAlarmTimer();
//        //AlarmTimer.setAlarmTimer(AlarmService.this,1000L,"test",AlarmManager.ELAPSED_REALTIME_WAKEUP,new CalendarDay());

            AlarmTimer.setAlarmTimer(this, triggerAtTime, intent.getStringExtra("event"), AlarmManager.ELAPSED_REALTIME_WAKEUP, new CalendarDay());

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //在Service结束后关闭AlarmManager
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.cancel(pi);

    }

}
