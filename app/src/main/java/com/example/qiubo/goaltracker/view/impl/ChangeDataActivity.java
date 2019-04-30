package com.example.qiubo.goaltracker.view.impl;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.qiubo.goaltracker.MainActivity;
import com.example.qiubo.goaltracker.R;
import com.example.qiubo.goaltracker.model.DO.Event;
import com.example.qiubo.goaltracker.model.DictationResult;
import com.example.qiubo.goaltracker.util.DateUtil;
import com.example.qiubo.goaltracker.util.SharedPreUtils;
import com.example.qiubo.goaltracker.util.StatusUtil;
import com.example.qiubo.goaltracker.util.TextChange;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import org.litepal.LitePal;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;
import jp.wasabeef.richeditor.RichEditor;

public class ChangeDataActivity extends AppCompatActivity implements View.OnClickListener{

    RichEditor mEditor;
    String editText;
    ImageView backImageView;
    TextView textViewStart,textViewEnd,textViewDate;
    LinearLayout cardViewStart,cardViewEnd;
    TimePickerDialog timePickerDialog;
    TextView textViewSave;
    List<String> tagList;
    FloatingActionButton buttonStart;
    LinearLayout linearLayoutDate;
    // 语音听写UI
    private RecognizerDialog mIatDialog;
    Event event;
    private TagContainerLayout mTagContainerLayout;
    private String contextUserId;
    private RadioGroup radioGroup;
    private RadioButton noramalRadioButton,importantRadioButton,busyRadioButton;
    private String level="1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_data);
        event=(Event) getIntent().getSerializableExtra("Event");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusUtil.setStatusBarColor(this,R.color.colorLucency);
        }

        //防止输入法顶起控件
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        SpeechUtility.createUtility(this,SpeechConstant.APPID +"=5c9cff16");
        mEditor=findViewById(R.id.change_data_edit_rich_edit);
        textViewStart=findViewById(R.id.change_data_text_view_start);
        textViewEnd=findViewById(R.id.change_data_text_view_end);
        cardViewEnd=findViewById(R.id.change_data_card_view_end);
        cardViewStart=findViewById(R.id.change_data_card_view_start);
        backImageView=findViewById(R.id.change_data_back);
        textViewSave=findViewById(R.id.change_data_text_view_save);
        buttonStart=findViewById(R.id.change_data_voice);
        textViewDate=findViewById(R.id.change_data_date);
        radioGroup=findViewById(R.id.change_data_level_group);
        noramalRadioButton=findViewById(R.id.change_data_level_normal);
        importantRadioButton=findViewById(R.id.change_data_level_important);
        busyRadioButton=findViewById(R.id.change_data_level_very_important);
        linearLayoutDate=findViewById(R.id.data_change_date_linear_layout_date);
        /**
         * 富文本編輯機器編輯
         */
        mEditor.setEditorHeight(200);
        mEditor.setEditorFontSize(22);
        mEditor.setPadding(10, 10, 10, 10);
        //mEditor.setPlaceholder("请输入内容...");
        mEditor.setHtml(event.getEvent());
        textViewStart.setText(TextChange.setTime(event.getPlanStartTime()));
        textViewEnd.setText(TextChange.setTime(event.getPlanEndTime()));
        mEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                editText=text;
            }
        });


        level=event.getLevel();
        if ("1".equals(level)){
            noramalRadioButton.setChecked(true);
        }
        if ("2".equals(level)){
            importantRadioButton.setChecked(true);
        }
        if ("3".equals(level)){
            busyRadioButton.setChecked(true);
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.change_data_level_normal:{
                        level="1";
                    };break;
                    case R.id.change_data_level_important:{
                        level="2";
                    };break;
                    case R.id.change_data_level_very_important:{
                        level="3";
                    };break;
                }
            }
        });
        SharedPreUtils sharedPreUtils=new SharedPreUtils(ChangeDataActivity.this);
        contextUserId= (String) SharedPreUtils.get("userId",null);

        cardViewStart.setOnClickListener(this);
        cardViewEnd.setOnClickListener(this);
        textViewSave.setOnClickListener(this);
        linearLayoutDate.setOnClickListener(this);
        backImageView.setOnClickListener(this);
        mIatDialog = new RecognizerDialog(ChangeDataActivity.this, mInitListener);
        mIatDialog.setParameter("sad","asd");
        buttonStart.setOnClickListener(this);
        textViewDate.setText(TextChange.changeTagShow(event.getPlanStartTime()));

    }


    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            Log.d("Note", "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                Toast.makeText(ChangeDataActivity.this, "初始化失败，错误码：" + code, Toast.LENGTH_SHORT).show();
            }
        }
    };
    @Override
    public void onClick(View view) {
        int id=view.getId();
        switch (id){
            case R.id.change_data_back:{
                this.finish();
            };break;
            case R.id.change_data_card_view_start:{
                Calendar calendar = Calendar.getInstance();
                int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                int currentMinute = calendar.get(Calendar.MINUTE);
                timePickerDialog = new TimePickerDialog(ChangeDataActivity.this,AlertDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {



                        textViewStart.setText(String.format("%02d:%02d", hourOfDay, minutes) );
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();
            };break;
            case R.id.change_data_card_view_end:{
                Calendar calendar = Calendar.getInstance();
                int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                int currentMinute = calendar.get(Calendar.MINUTE);
                timePickerDialog = new TimePickerDialog(ChangeDataActivity.this,AlertDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {



                        textViewEnd.setText(String.format("%02d:%02d", hourOfDay, minutes) );
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();
            };break;
            case R.id.change_data_text_view_save:{
//                List<Event>eventList=new ArrayList<>();
//                for (String s:tagList){
//                    Event event=new Event();
//                    String startTemp= (String) textViewStart.getText();
//                    String endTemp= (String) textViewEnd.getText();
//                    event.setPlanStartTime(TextChange.mergeTime(s,startTemp));
//                    event.setPlanEndTime(TextChange.mergeTime(s,endTemp));
//                    editText=mEditor.getHtml();
//                    event.setEvent(editText);
//                    event.setDone(false);
//                    eventList.add(event);
//                }
//                LitePal.saveAll(eventList);
                Event eventTemp=LitePal.find(Event.class,event.getId());

                eventTemp.setEvent(mEditor.getHtml());
                String result=textViewDate.getText().toString().substring(0,4);
                result+=textViewDate.getText().toString().substring(5,7);
                result+=textViewDate.getText().toString().substring(8,textViewDate.getText().toString().length()-1);

                String timeTemp=getTime(textViewStart.getText().toString());
                eventTemp.setPlanStartTime(result+timeTemp);
                timeTemp=getTime(textViewEnd.getText().toString());
                eventTemp.setPlanEndTime(result+timeTemp);
                if (contextUserId==null){
                    eventTemp.setUserId(0L);
                }else {
                    eventTemp.setUserId(Long.valueOf(contextUserId));
                }
                if ("1".equals(level)){
                    eventTemp.setLevel(level);

                }
                if ("2".equals(level)){
                    eventTemp.setLevel(level);

                }
                if ("3".equals(level)){
                    eventTemp.setLevel(level);

                }
                eventTemp.save();
                Intent intent = new Intent(ChangeDataActivity.this,MainActivity.class);
                startActivity(intent);
            };break;
            case R.id.change_data_voice:{

                mIatDialog.setListener(new RecognizerDialogListener() {
                    String resultJson = "[";//放置在外边做类的变量则报错，会造成json格式不对（？）
                    @Override
                    public void onResult(RecognizerResult recognizerResult, boolean b) {
                        if (!b) {
                            resultJson += recognizerResult.getResultString() + ",";
                        } else {
                            resultJson += recognizerResult.getResultString() + "]";
                        }
                        if (b) {
                            //解析语音识别后返回的json格式的结果
                            Gson gson = new Gson();
                            List<DictationResult> resultList = gson.fromJson(resultJson,
                                    new TypeToken<List<DictationResult>>() {
                                    }.getType());
                            String result = "";
                            for (int i = 0; i < resultList.size() - 1; i++) {
                                result += resultList.get(i).toString();
                            }
                            System.out.println("结果:"+result);
                            //Toast.makeText(NoteEventActivity.this, result, Toast.LENGTH_LONG).show();
                            if (mEditor.getHtml()!=null) {
                                result = mEditor.getHtml() + result;
                            }
                            mEditor.setHtml(result);
                        }
                        System.out.println("调用onResult方法:"+recognizerResult.getResultString());
                    }

                    @Override
                    public void onError(SpeechError speechError) {
                        System.out.println("zzzzz");
                    }
                });
                mIatDialog.show();
            };break;
            case R.id.data_change_date_linear_layout_date:{
                Calendar calendar = null ;
                try {
                    String time= (String) textViewDate.getText();
                    String result=time.substring(0,4);
                    result+=time.substring(5,7);
                    result+=time.substring(8,time.length()-1);
                    calendar = DateUtil.changeStringToYMD(result);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                DatePickerDialog datePickerDialog=new DatePickerDialog(ChangeDataActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        textViewDate.setText(String.format("%4d年%02d月%02d日",i,i1+1,i2));
                    }
                },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DATE));
                datePickerDialog.show();
            };break;
        }
    }

    String getTime(String timeTemp){

        String[] time=timeTemp.split(":");
        String strTime=new String();
        strTime+=time[0];
        strTime+=time[1];
        return strTime;
    }

}
