package com.example.qiubo.goaltracker.view.impl;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qiubo.goaltracker.R;
import com.example.qiubo.goaltracker.model.DO.Event;
import com.example.qiubo.goaltracker.receiver.ScreenOffAdminReceiver;
import com.example.qiubo.goaltracker.service.LockService;
import com.example.qiubo.goaltracker.util.DateUtil;
import com.example.qiubo.goaltracker.util.SharedPreUtils;
import com.example.qiubo.goaltracker.util.StatusUtil;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.util.Calendar;

public class AlarmDialogActivity extends AppCompatActivity implements View.OnClickListener{

    TextView textViewEvent,textViewTime;
    Button startButton;
    //boolean isScreenOn=true;

    private DevicePolicyManager policyManager;
    private ComponentName adminReceiver;
    private PowerManager mPowerManager;
    private PowerManager.WakeLock mWakeLock;
    private Event event;
    private  String time;
    private int hour ,minute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_dialog);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusUtil.setStatusBarColor(this,R.color.colorLucency);
        }
        adminReceiver = new ComponentName(AlarmDialogActivity.this, ScreenOffAdminReceiver.class);
        mPowerManager = (PowerManager) getSystemService(POWER_SERVICE);
        policyManager = (DevicePolicyManager) AlarmDialogActivity.this.getSystemService(Context.DEVICE_POLICY_SERVICE);
        checkAndTurnOnDeviceManager(null);


        textViewEvent=findViewById(R.id.alarm_dialog_event);
        textViewTime=findViewById(R.id.alarm_dialog_time);
        startButton=findViewById(R.id.alarm_dialog_start);
        startButton.setOnClickListener(this);
        event= (Event) getIntent().getSerializableExtra("event");
        Calendar eventStartCalender=null;
        Calendar eventEndCalender=null;
        try {
           eventStartCalender =DateUtil.changeStringToDate(event.getPlanStartTime());
          eventEndCalender =DateUtil.changeStringToDate(event.getPlanEndTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        hour=eventEndCalender.get(Calendar.HOUR_OF_DAY)-eventStartCalender.get(Calendar.HOUR_OF_DAY);
        minute=eventEndCalender.get(Calendar.MINUTE)-eventStartCalender.get(Calendar.MINUTE);
        textViewEvent.setText(event.getEvent());
         time =hour+"小时"+minute+"分";
        textViewTime.setText(time);
    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        switch (id){
            case R.id.alarm_dialog_start:{

                screenOff();
                SharedPreUtils sharedPreUtils=new SharedPreUtils(AlarmDialogActivity.this);
                SharedPreUtils.put("ScreenEvent",event.getEvent());
                SharedPreUtils.put("ScreenHour",String.valueOf(hour));
                SharedPreUtils.put("ScreenMinute",String.valueOf(minute));
                SharedPreUtils.put("ScreenId",String.valueOf(event.getId()));
                Intent service = new Intent(this, LockService.class);

                startService(service);

            };break;
        }
    }

    private void screenOff(){
        boolean admin = policyManager.isAdminActive(adminReceiver);
        if (admin) {
            policyManager.lockNow();
        } else {
            Toast.makeText(this,"没有设备管理权限",
                    Toast.LENGTH_LONG).show();

        }


    }

    /**
     * @param view 检测并去激活设备管理器权限
     */
    public void checkAndTurnOnDeviceManager(View view) {
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, adminReceiver);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "开启后就可以使用锁屏功能了...");//显示位置见图二
        startActivityForResult(intent, 0);

    }

}
