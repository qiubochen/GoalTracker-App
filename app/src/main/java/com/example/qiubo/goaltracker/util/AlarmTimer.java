package com.example.qiubo.goaltracker.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import com.example.qiubo.goaltracker.receiver.AlarmReceiver;
import com.prolificinteractive.materialcalendarview.CalendarDay;

/**
 * 闹钟定时工具类
 *
 * @author xulei
 * @time 2016/12/13 10:03
 */

public class AlarmTimer {

    /**
     * 设置周期性闹钟
     *
     * @param context
     * @param firstTime
     * @param cycTime
     * @param action
     * @param AlarmManagerType 闹钟的类型，常用的有5个值：AlarmManager.ELAPSED_REALTIME、
     *       AlarmManager.ELAPSED_REALTIME_WAKEUP、AlarmManager.RTC、
     *       AlarmManager.RTC_WAKEUP、AlarmManager.POWER_OFF_WAKEUP
     */
    public static void setRepeatingAlarmTimer(Context context, long firstTime,
                                              long cycTime, String action, int AlarmManagerType) {
        Intent myIntent = new Intent();
        myIntent.setAction(action);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, myIntent, 0);
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.setRepeating(AlarmManagerType, firstTime, cycTime, sender);
        //param1：闹钟类型，param1：闹钟首次执行时间，param1：闹钟两次执行的间隔时间，param1：闹钟响应动作。
    }

    /**
     * 设置定时闹钟
     *
     * @param context
     * @param cycTime
     * @param action
     * @param AlarmManagerType 闹钟的类型，常用的有5个值：AlarmManager.ELAPSED_REALTIME、
     *       AlarmManager.ELAPSED_REALTIME_WAKEUP、AlarmManager.RTC、
     *       AlarmManager.RTC_WAKEUP、AlarmManager.POWER_OFF_WAKEUP
     */
    public static void setAlarmTimer(Context context, long cycTime,
                                     String action, int AlarmManagerType, CalendarDay date) {
        Intent myIntent = new Intent(context,AlarmReceiver.class);
        //传递定时日期
        myIntent.putExtra("event", action);
        //myIntent.setAction(action);
        SharedPreUtils sharedPreUtils=new SharedPreUtils(context);
        //给每个闹钟设置不同ID防止覆盖
        int alarmId = (Integer) SharedPreUtils.get("alarm_id", 0);
        sharedPreUtils.put("alarm_id", ++alarmId);
        PendingIntent sender = PendingIntent.getBroadcast(context, alarmId, myIntent, 0);
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        //alarm.set(AlarmManagerType, cycTime, sender);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarm.setWindow(AlarmManager.ELAPSED_REALTIME_WAKEUP, cycTime, 1000, sender);
        }
    }

    /**
     * 取消闹钟
     *
     * @param context
     * @param action
     */
    public static void cancelAlarmTimer(Context context, String action) {
        Intent myIntent = new Intent();
        myIntent.setAction(action);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, myIntent,0);
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(sender);
    }
}