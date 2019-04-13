package com.example.qiubo.goaltracker.view.impl;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.qiubo.goaltracker.MainActivity;
import com.example.qiubo.goaltracker.R;
import com.example.qiubo.goaltracker.model.DO.Event;
import com.example.qiubo.goaltracker.model.DictationResult;
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
import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


import co.lujun.androidtagview.TagContainerLayout;
import jp.wasabeef.richeditor.RichEditor;

public class NoteEventActivity extends AppCompatActivity implements View.OnClickListener{
    RichEditor mEditor;
    String editText;
    ImageView backImageView;
    TextView textViewStart,textViewEnd;
    LinearLayout cardViewStart,cardViewEnd;
    TimePickerDialog timePickerDialog;
    TextView textViewSave;
    List<String> tagList;
    FloatingActionButton buttonStart;
    // 语音听写UI
    private RecognizerDialog mIatDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_event);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusUtil.setStatusBarColor(this,R.color.colorLucency);
        }

        //防止输入法顶起控件
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        SpeechUtility.createUtility(this,SpeechConstant.APPID +"=5c9cff16");
        mEditor=findViewById(R.id.note_edit_rich_edit);
        textViewStart=findViewById(R.id.note_event_text_view_start);
        textViewEnd=findViewById(R.id.note_event_text_view_end);
        cardViewEnd=findViewById(R.id.note_event_card_view_end);
        cardViewStart=findViewById(R.id.note_event_card_view_start);
        backImageView=findViewById(R.id.note_event_back);
        textViewSave=findViewById(R.id.note_event_text_view_save);
        buttonStart=findViewById(R.id.note_event_voice);
        /**
         * 富文本編輯機器編輯
         */
        mEditor.setEditorHeight(200);
        mEditor.setEditorFontSize(22);
        mEditor.setPadding(10, 10, 10, 10);
        mEditor.setPlaceholder("请输入内容...");

        mEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                editText=text;
            }
        });
        //mEditor.setInputEnabled(false);
        /**
         * 获取tag标签
         */
        Intent intent=getIntent();
        tagList =intent.getStringArrayListExtra("labelList");
        /**
         * 绘制taglayout
         */
        TagContainerLayout mTagContainerLayout = (TagContainerLayout) findViewById(R.id.note_event_tags);

        mTagContainerLayout.setTags(tagList);

        cardViewStart.setOnClickListener(this);
        cardViewEnd.setOnClickListener(this);
        textViewSave.setOnClickListener(this);

        backImageView.setOnClickListener(this);
        mIatDialog = new RecognizerDialog(NoteEventActivity.this, mInitListener);
        mIatDialog.setParameter("sad","asd");
        buttonStart.setOnClickListener(this);

    }
    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            Log.d("Note", "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                Toast.makeText(NoteEventActivity.this, "初始化失败，错误码：" + code, Toast.LENGTH_SHORT).show();
            }
        }
    };
    @Override
    public void onClick(View view) {
       int id=view.getId();
       switch (id){
           case R.id.note_event_back:{
               this.finish();
           };break;
           case R.id.note_event_card_view_start:{
               Calendar calendar = Calendar.getInstance();
               int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
               int currentMinute = calendar.get(Calendar.MINUTE);
               timePickerDialog = new TimePickerDialog(NoteEventActivity.this,AlertDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
                   @Override
                   public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {



                       textViewStart.setText(String.format("%02d:%02d", hourOfDay, minutes) );
                   }
               }, currentHour, currentMinute, false);

               timePickerDialog.show();
           };break;
           case R.id.note_event_card_view_end:{
               Calendar calendar = Calendar.getInstance();
               int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
               int currentMinute = calendar.get(Calendar.MINUTE);
               timePickerDialog = new TimePickerDialog(NoteEventActivity.this,AlertDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
                   @Override
                   public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {



                       textViewEnd.setText(String.format("%02d:%02d", hourOfDay, minutes) );
                   }
               }, currentHour, currentMinute, false);

               timePickerDialog.show();
           };break;
           case R.id.note_event_text_view_save:{
              List<Event>eventList=new ArrayList<>();
              for (String s:tagList){
                  Event event=new Event();
                  String startTemp= (String) textViewStart.getText();
                  String endTemp= (String) textViewEnd.getText();
                  event.setPlanStartTime(TextChange.mergeTime(s,startTemp));
                  event.setPlanEndTime(TextChange.mergeTime(s,endTemp));
                  editText=mEditor.getHtml();
                  event.setEvent(editText);
                  event.setDone(false);
                  eventList.add(event);
              }
               LitePal.saveAll(eventList);
               Intent intent = new Intent(NoteEventActivity.this,MainActivity.class);
               startActivity(intent);
           };break;
           case R.id.note_event_voice:{

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
       }
    }

}
