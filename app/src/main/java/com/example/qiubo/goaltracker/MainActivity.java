package com.example.qiubo.goaltracker;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.qiubo.goaltracker.adapter.MainFragmentAdapter;
import com.example.qiubo.goaltracker.model.BaseResult;
import com.example.qiubo.goaltracker.model.DO.Event;
import com.example.qiubo.goaltracker.model.DO.User;
import com.example.qiubo.goaltracker.model.ResponseUser;
import com.example.qiubo.goaltracker.util.InternetRetrofit;
import com.example.qiubo.goaltracker.util.SharedPreUtils;
import com.example.qiubo.goaltracker.util.StatusUtil;
import com.example.qiubo.goaltracker.view.NoScrollViewPager;
import com.example.qiubo.goaltracker.view.impl.DataFragment;
import com.example.qiubo.goaltracker.view.impl.EventFragment;
import com.example.qiubo.goaltracker.view.impl.GroupFragment;
import com.example.qiubo.goaltracker.view.impl.PersonFragment;
import com.example.qiubo.goaltracker.view.impl.RegisterActivity;
import com.example.qiubo.goaltracker.view.impl.VerifyActivity;
import com.google.gson.JsonObject;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,DataFragment.OnFragmentInteractionListener,EventFragment.OnFragmentInteractionListener,GroupFragment.OnFragmentInteractionListener,PersonFragment.OnFragmentInteractionListener{
   //Button button;
    TabLayout tableLayout;
    final String PERSON= "个人",EVENT="事件",DATA="日程",GROUP="数据";
    NoScrollViewPager viewPager;
    final String TAG = "Main Activity";
    private final String IPAdress="http://39.108.227.213:8080/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //button=findViewById(R.id.button);
       // button.setOnClickListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            StatusUtil.setStatusBarColor(this,R.color.colorLucency);
        }
        initView();
        List<Event>eventList=LitePal.findAll(Event.class);
        System.out.println("--=-=-");
        for (Event e:eventList){
            System.out.println(e.getEvent());
            System.out.println(e.getPlanStartTime());
            System.out.println(e.getDone());
            System.out.println(e.getCompleteTime());
        }


        //LitePal.deleteAll(Event.class);
    }
        void initView(){
            tableLayout=findViewById(R.id.main_tablayout);
            viewPager=findViewById(R.id.main_pageview);
            viewPager.setScanScroll(false);
            tableLayout.addTab(tableLayout.newTab().setText(PERSON));
            tableLayout.addTab(tableLayout.newTab().setText(EVENT));
            tableLayout.addTab(tableLayout.newTab().setText(DATA));
            tableLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    Log.i(TAG,"onTabSelected:"+tab.getText());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
            tableLayout.setupWithViewPager(viewPager);

            List<Fragment>fragmentList=new ArrayList<>();
            fragmentList.add(PersonFragment.newInstance("person","test"));
            fragmentList.add(EventFragment.newInstance("event","test"));
            fragmentList.add(DataFragment.newInstance("data","test"));
           fragmentList.add(GroupFragment.newInstance("group","test"));

            List<String>titleList=new ArrayList<>();
            titleList.add(PERSON);
            titleList.add(EVENT);
            titleList.add(DATA);
           titleList.add(GROUP);

            MainFragmentAdapter mainFragmentAdapter =new MainFragmentAdapter(getSupportFragmentManager(),fragmentList,titleList);
            //viewPager.setOffscreenPageLimit(4);
            viewPager.setAdapter(mainFragmentAdapter);
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float v, int i1) {

                }

                @Override
                public void onPageSelected(int i) {
                    Log.i(TAG,"select page:"+i);
                }

                @Override
                public void onPageScrollStateChanged(int i) {

                }
            });
        }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        switch (id){
//            case R.id.button:{
//                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                startActivity(intent);
//            };break;
        }

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }



}
