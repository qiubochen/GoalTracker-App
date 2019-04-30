package com.example.qiubo.goaltracker.view.impl;

import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.qiubo.goaltracker.R;
import com.example.qiubo.goaltracker.model.DO.Event;
import com.example.qiubo.goaltracker.receiver.HomeReceiver;
import com.example.qiubo.goaltracker.receiver.ScreenOffAdminReceiver;
import com.example.qiubo.goaltracker.service.LockService;
import com.example.qiubo.goaltracker.util.SharedPreUtils;
import com.example.qiubo.goaltracker.util.StatusUtil;

import org.litepal.LitePal;
import org.w3c.dom.Text;

public class LockScreenActivity extends AppCompatActivity implements View.OnClickListener{

    TextView eventTextView,timeTextView;
    String TAG="LockScreenActivity";
    long eventId;
    Button stopButton;
    CountDownTimer timer;
    HomeReceiver innerReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusUtil.setStatusBarColor(this,R.color.colorLucency);
        }


        this.getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

        eventTextView=findViewById(R.id.lock_event);
        timeTextView=findViewById(R.id.lock_time);
        stopButton=findViewById(R.id.lock_stop);
        stopButton.setOnClickListener(LockScreenActivity.this);
        SharedPreUtils sharedPreUtils=new SharedPreUtils(LockScreenActivity.this);
        String s= (String) SharedPreUtils.get("ScreenEvent",null);
        String sHour= (String) SharedPreUtils.get("ScreenHour",null);
        String sMinute = (String) SharedPreUtils.get("ScreenMinute",null);
        eventId=Long.valueOf((String) SharedPreUtils.get("ScreenId",null));
        eventTextView.setText(sHour+"小时"+sMinute+"分");
        long timeStamp=(Integer.valueOf(sHour)*60*60+Integer.valueOf(sMinute)*60);
        startCountDownTime(timeStamp);
        eventTextView.setText(s);
        innerReceiver = new HomeReceiver();                                                        //注册广播
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        registerReceiver(innerReceiver, intentFilter);
    }

    private void startCountDownTime(long time) {
        /**
         * 最简单的倒计时类，实现了官方的CountDownTimer类（没有特殊要求的话可以使用）
         * 即使退出activity，倒计时还能进行，因为是创建了后台的线程。
         * 有onTick，onFinsh、cancel和start方法
         */
          timer= new CountDownTimer(time * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //每隔countDownInterval秒会回调一次onTick()方法
                Log.d(TAG, "onTick  " + millisUntilFinished );
                long hour,minute;
                millisUntilFinished=millisUntilFinished/60000;
                Log.d(TAG, "onTick  " + millisUntilFinished );
                hour=millisUntilFinished/60;
                minute=millisUntilFinished%60;

                String s=hour+"小时"+minute+"分";
                timeTextView.setText(s);
            }

            @Override
            public void onFinish() {

                ContentValues values=new ContentValues();
                values.put("done","1");
                LitePal.update(Event.class,values,eventId);
                Intent intent=new Intent(LockScreenActivity.this,LockService.class);
                stopService(intent);
                unregisterReceiver(innerReceiver);                                             //取消注册

                finish();
                Log.d(TAG, "onFinish -- 倒计时结束");

            }
        };
        timer.start();// 开始计时
        //timer.cancel(); // 取消
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    protected void onStop() {

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(innerReceiver);
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        switch (id){
            case R.id.lock_stop:{
                Intent intent=new Intent(LockScreenActivity.this,LockService.class);
                stopService(intent);
                timer.cancel();
                finish();
            };break;
        }
    }
}
