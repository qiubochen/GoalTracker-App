package com.example.qiubo.goaltracker.receiver;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.qiubo.goaltracker.MainActivity;
import com.example.qiubo.goaltracker.R;
import com.example.qiubo.goaltracker.service.AlarmService;
import com.example.qiubo.goaltracker.util.SharedPreUtils;

import java.io.File;
import java.io.IOException;

import static android.content.Context.NOTIFICATION_SERVICE;
import static android.support.v4.content.ContextCompat.getSystemService;

public class AlarmReceiver extends BroadcastReceiver {

    String NOTIFYID="notify_id";
    public static int inc=0;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public synchronized void onReceive(final Context context, Intent intent) {
//        //设置通知内容并在onReceive()这个函数执行时开启
//        if (!mediaPlayer.isPlaying()){
//            mediaPlayer.start();
//        }
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle("提醒");
//        builder.setMessage("该补水啦" );
//        builder.setCancelable(false);
//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                mediaPlayer.reset();
//                initMediaPlayer(context);
//            }
//        });
//        final AlertDialog dialog = builder.create();
//        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//        dialog.show();

        Log.i("AlarmReceiver","闹钟响了");

//        String id = "my_channel_01";
//        String name="我是渠道名字";
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
//        Notification notification = null;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel mChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH);
//
//            mChannel.enableVibration(true);//震动可用
//            AudioAttributes audioAttributes = new AudioAttributes.Builder()
//                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
//                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
//                    .build();
//            Uri soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"+ context.getApplicationContext().getPackageName() + "/" + R.raw.alarm);
//            mChannel.setSound(soundUri, audioAttributes); //设置没有声音
//
//            notificationManager.createNotificationChannel(mChannel);
//            notification = new Notification.Builder(context)
//                    .setChannelId(id)
//                    .setContentTitle("5 new messages")
//                    .setContentText("hahaha")
//                    .setSmallIcon(R.mipmap.ic_launcher)
//                    .setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE).build();

//        notificationManager.notify(111123, notification);


        String id = "channel_0";
        String des = "111";
        Notification notification = null;
        String uuId=String.valueOf(System.currentTimeMillis());
        int notifyId=  Integer.parseInt(uuId.substring(uuId.length()-8));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(id, des, NotificationManager.IMPORTANCE_HIGH);
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
                notificationManager.createNotificationChannel(channel);
                notification = new Notification.Builder(context, id)
                        .setContentTitle("提醒您: "+intent.getStringExtra("event"))
                        //.setContentText(intent.getStringExtra("event"))
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setStyle(new Notification.MediaStyle())
                        .setAutoCancel(false)
                        .addExtras(new Bundle())
                        .build();
                notificationManager.notify(notifyId, notification);
            } else {
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                        .setContentTitle("5 new messages")
                        .setContentText("hahaha")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE).setOngoing(true);
                // .setChancnel(id);//无效
                notification = notificationBuilder.build();
            }
            System.out.println("AlarmReceiver的提醒");


    }




}
