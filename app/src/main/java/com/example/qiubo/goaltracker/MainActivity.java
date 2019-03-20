package com.example.qiubo.goaltracker;

import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.qiubo.goaltracker.adapter.MainFragmentAdapter;
import com.example.qiubo.goaltracker.model.DO.Event;
import com.example.qiubo.goaltracker.view.NoScrollViewPager;
import com.example.qiubo.goaltracker.view.impl.DataFragment;
import com.example.qiubo.goaltracker.view.impl.EventFragment;
import com.example.qiubo.goaltracker.view.impl.GroupFragment;
import com.example.qiubo.goaltracker.view.impl.PersonFragment;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,DataFragment.OnFragmentInteractionListener,EventFragment.OnFragmentInteractionListener,GroupFragment.OnFragmentInteractionListener,PersonFragment.OnFragmentInteractionListener{
   //Button button;
    TabLayout tableLayout;
    final String PERSON= "个人",EVENT="事件",DATA="数据",GROUP="群体";
    NoScrollViewPager viewPager;
    final String TAG = "Main Activity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //button=findViewById(R.id.button);
       // button.setOnClickListener(this);
        initView();
        List<Event>eventList=LitePal.findAll(Event.class);
        for (Event e:eventList){
            System.out.println(e.getEvent());
            System.out.println(e.getPlanStartTime());
        }
        LitePal.deleteAll(Event.class);
    }
        void initView(){
            tableLayout=findViewById(R.id.main_tablayout);
            viewPager=findViewById(R.id.main_pageview);
            viewPager.setScanScroll(false);
            tableLayout.addTab(tableLayout.newTab().setText(PERSON));
            tableLayout.addTab(tableLayout.newTab().setText(GROUP));
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
            viewPager.setOffscreenPageLimit(4);
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
