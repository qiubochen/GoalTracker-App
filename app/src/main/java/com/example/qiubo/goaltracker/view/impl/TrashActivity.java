package com.example.qiubo.goaltracker.view.impl;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.qiubo.goaltracker.R;
import com.example.qiubo.goaltracker.adapter.TimeLineViewAdapter;
import com.example.qiubo.goaltracker.decorator.DividerItemDecoration;
import com.example.qiubo.goaltracker.model.DO.Event;
import com.example.qiubo.goaltracker.util.StatusUtil;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class TrashActivity extends AppCompatActivity {

    private RecyclerView Rv;
    private ArrayList<HashMap<String,Object>> listItem;
    private TimeLineViewAdapter myAdapter;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trash);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusUtil.setStatusBarColor(this,R.color.colorLucency);
        }
        // 初始化显示的数据
        initData();

        // 绑定数据到RecyclerView
        initView();
    }

    // 初始化显示的数据
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void initData(){
        listItem = new ArrayList<HashMap<String, Object>>();/*在数组中存放数据*/
        List<Event>eventList=LitePal.where("done = ?","1").find(Event.class);

        eventList=eventList.stream().sorted(Comparator.comparing(Event::getCompleteTime).reversed()).collect(Collectors.toList());
        for (Event event:eventList){
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemTitle", event.getEvent());
            listItem.add(map);
        }
//        HashMap<String, Object> map1 = new HashMap<String, Object>();
//        HashMap<String, Object> map2 = new HashMap<String, Object>();
//        HashMap<String, Object> map3 = new HashMap<String, Object>();
//        HashMap<String, Object> map4 = new HashMap<String, Object>();
//        HashMap<String, Object> map5 = new HashMap<String, Object>();
//        HashMap<String, Object> map6 = new HashMap<String, Object>();
//
//        map1.put("ItemTitle", "美国谷歌公司已发出");
//        map1.put("ItemText", "发件人:谷歌 CEO Sundar Pichai");
//        listItem.add(map1);
//
//        map2.put("ItemTitle", "国际顺丰已收入");
//        map2.put("ItemText", "等待中转");
//        listItem.add(map2);
//
//        map3.put("ItemTitle", "国际顺丰转件中");
//        map3.put("ItemText", "下一站中国");
//        listItem.add(map3);
//
//        map4.put("ItemTitle", "中国顺丰已收入");
//        map4.put("ItemText", "下一站广州华南理工大学");
//        listItem.add(map4);
//
//        map5.put("ItemTitle", "中国顺丰派件中");
//        map5.put("ItemText", "等待派件");
//        listItem.add(map5);
//
//        map6.put("ItemTitle", "华南理工大学已签收");
//        map6.put("ItemText", "收件人:Carson");
//        listItem.add(map6);
    }

    // 绑定数据到RecyclerView
    public void initView(){
        Rv = (RecyclerView) findViewById(R.id.timeline);
        //使用线性布局
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        Rv.setLayoutManager(layoutManager);
        Rv.setHasFixedSize(true);

        //用自定义分割线类设置分割线
        Rv.addItemDecoration(new DividerItemDecoration(this));

        //为ListView绑定适配器
        myAdapter = new TimeLineViewAdapter(this,listItem);
        Rv.setAdapter(myAdapter);
    }


}
