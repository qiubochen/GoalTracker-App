package com.example.qiubo.goaltracker.view.impl;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.qiubo.goaltracker.MainActivity;
import com.example.qiubo.goaltracker.R;
import com.example.qiubo.goaltracker.model.DO.Event;
import com.example.qiubo.goaltracker.util.TextChange;

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
    CardView cardViewStart,cardViewEnd;
    TimePickerDialog timePickerDialog;
    TextView textViewSave;
    List<String> tagList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_event);
        mEditor=findViewById(R.id.note_edit_rich_edit);
        textViewStart=findViewById(R.id.note_event_text_view_start);
        textViewEnd=findViewById(R.id.note_event_text_view_end);
        cardViewEnd=findViewById(R.id.note_event_card_view_end);
        cardViewStart=findViewById(R.id.note_event_card_view_start);
        backImageView=findViewById(R.id.note_event_back);
        textViewSave=findViewById(R.id.note_event_text_view_save);

        /**
         * 富文本編輯機器編輯
         */
        mEditor.setEditorHeight(200);
        mEditor.setEditorFontSize(22);
        mEditor.setPadding(10, 10, 10, 10);
        mEditor.setPlaceholder("Insert text here...");
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



    }

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
                  event.setEvent(editText);
                  eventList.add(event);
              }
               LitePal.saveAll(eventList);
               Intent intent = new Intent(NoteEventActivity.this,MainActivity.class);
               startActivity(intent);
           };break;
       }
    }
}
