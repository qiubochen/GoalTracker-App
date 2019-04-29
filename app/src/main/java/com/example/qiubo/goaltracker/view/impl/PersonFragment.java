package com.example.qiubo.goaltracker.view.impl;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.qiubo.goaltracker.MainActivity;
import com.example.qiubo.goaltracker.R;
import com.example.qiubo.goaltracker.adapter.FragmentPersonRecycleViewAdapter;
import com.example.qiubo.goaltracker.model.BaseResult;
import com.example.qiubo.goaltracker.model.DO.Event;
import com.example.qiubo.goaltracker.model.DO.User;
import com.example.qiubo.goaltracker.service.AlarmService;
import com.example.qiubo.goaltracker.util.DateUtil;
import com.example.qiubo.goaltracker.util.InternetRetrofit;
import com.example.qiubo.goaltracker.util.SharedPreUtils;
import com.google.gson.JsonArray;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.yanzhenjie.recyclerview.OnItemClickListener;
import com.yanzhenjie.recyclerview.OnItemMenuClickListener;
import com.yanzhenjie.recyclerview.SwipeMenu;
import com.yanzhenjie.recyclerview.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import org.litepal.LitePal;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PersonFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PersonFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private ImageView userImageView,refreshImageView;
    private MaterialSearchView searchView;
    private Toolbar toolbar;
    private SwipeRecyclerView swipeRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FragmentPersonRecycleViewAdapter fragmentPersonRecycleViewAdapter;
    private List<Event>datas;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private TextView textViewNickName;
    private final String IPAdress="http://39.108.227.213:8080/";
    private String contextUserId;
    public PersonFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PersonFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PersonFragment newInstance(String param1, String param2) {
        PersonFragment fragment = new PersonFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        System.out.println("asdd==========asd");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_person, container, false);

        toolbar=view.findViewById(R.id.fragment_person_toolbar);
        swipeRecyclerView=view.findViewById(R.id.fragment_person_recycle_view);
        swipeRefreshLayout=view.findViewById(R.id.person_refresh_layout);
        navigationView=view.findViewById(R.id.person_navigation_view);
        drawerLayout=view.findViewById(R.id.person_drawer_layout);

        searchView = (MaterialSearchView) view.findViewById(R.id.fragment_person_search_view);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onQueryTextSubmit(String query) {
                datas.clear();
                if (contextUserId==null) {
                    datas.addAll(LitePal.where("event like ? and done = ? and userId = ?", "%" + query + "%", "0", "0").find(Event.class));

                }else {
                    datas.addAll(LitePal.where("event like ? and done = ? and userId = ?", "%" + query + "%", "0", contextUserId).find(Event.class));
                }
                //datas= datas.stream().sorted(Comparator.comparing(Event::getPlanStartTime)).collect(Collectors.toList());
                loadData();
                return true;
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onQueryTextChange(String newText) {
//                datas.clear();
//                if (contextUserId==null) {
//                    datas.addAll(LitePal.where("event like ? and done = ? and userId = ?", "%" + newText + "%", "0", "0").find(Event.class));
//                }else {
//                    datas.addAll(LitePal.where("event like ? and done = ? and userId = ?", "%" + newText + "%", "0", contextUserId).find(Event.class));
//                }
//                datas= datas.stream().sorted(Comparator.comparing(Event::getPlanStartTime)).collect(Collectors.toList());
//                loadData();
                return true;
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.user:{
                        Intent intent =new Intent(getActivity(),LoginActivity.class);
                        startActivity(intent);
                    };break;

                }
                return false;
            }
        });

        SharedPreUtils sharedPreUtils=new SharedPreUtils(getActivity());
        contextUserId= (String) SharedPreUtils.get("userId",null);
        initToolbar();
        initNavigaitonView();
        initHeaderNickName(getActivity());
        return view;
    }

    void initHeaderNickName(Context context){

        View navigationViewHeader=navigationView.getHeaderView(0);
        textViewNickName=navigationViewHeader.findViewById(R.id.header_nick_name);

        SharedPreUtils sharedPreUtils=new SharedPreUtils(getActivity());
        String s= (String) SharedPreUtils.get("nickName",null);

        if (s!=null){
            textViewNickName.setText(s);

        }
    }

    private void initNavigaitonView(){
//设置显示左上角的返回键
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //给左上角的图标加上开关属性，这个是套路，少一个步骤都触发不了开关效果。
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar,R.string.open_drawer,R.string.close_drawer);
        //该方法会自动和actionBar关联, 将开关的图片显示在了action上，如果不设置，也可以有抽屉的效果，不过是默认的图标
        mDrawerToggle.syncState();
        drawerLayout.addDrawerListener(mDrawerToggle);

    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initRecycleView();
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        switch (id){

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        System.out.println("======hahaha======");
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        //setSearchView(searchView);

    }

    private  void initToolbar() {
        toolbar.setTitle("");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
    }

    /**
     * 创建recycleview
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initRecycleView(){
        swipeRecyclerView.setOnItemClickListener(mItemClickListener);
        swipeRecyclerView.setSwipeMenuCreator(mSwipeMenuCreator);
        swipeRecyclerView.setOnItemMenuClickListener(mItemMenuClickListener);
        swipeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        swipeRefreshLayout.setOnRefreshListener(mRefreshListener);
        datas=initdata();
         fragmentPersonRecycleViewAdapter=new FragmentPersonRecycleViewAdapter(getActivity(),datas);
        swipeRecyclerView.setAdapter(fragmentPersonRecycleViewAdapter);
    }

    /**
     * 获取recycleview的列表数据
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<Event> initdata(){
        List<Event> datasTemp;
        if (contextUserId==null) {
           datasTemp = LitePal.where("done = ? and userId = ?", "0","0").find(Event.class);
        }else {
            datasTemp=LitePal.where("done = ? and userId = ?","0",contextUserId).find(Event.class);
        }
        datasTemp= datasTemp.stream().sorted(Comparator.comparing(Event::getPlanStartTime)).collect(Collectors.toList());
        return datasTemp;
    }
    /**
     * 刷新数据
     */
    private void loadData(){

        fragmentPersonRecycleViewAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
        // 第一次加载数据：一定要调用这个方法，否则不会触发加载更多。
        // 第一个参数：表示此次数据是否为空，假如你请求到的list为空(== null || list.size == 0)，那么这里就要true。
        // 第二个参数：表示是否还有更多数据，根据服务器返回给你的page等信息判断是否还有更多，这样可以提供性能，如果不能判断则传true。
       if (datas.size()==0||datas==null)
           swipeRecyclerView.loadMoreFinish(true,true);
       else
        swipeRecyclerView.loadMoreFinish(false, true);
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    /**
     * 刷新的事件
     */
    private SwipeRefreshLayout.OnRefreshListener mRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onRefresh() {
            datas.clear();
            datas.addAll(initdata());
           loadData();


            if (contextUserId!=null){
                List<Event>eventList=LitePal.findAll(Event.class);
                initEventData();

            }
        }
    };
    /**
     * RecyclerView的Item点击监听。
     */
    private OnItemClickListener mItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(View itemView, int position) {
            Intent intent =new Intent(getActivity(),ChangeDataActivity.class);
            intent.putExtra("Event",datas.get(position));
            startActivity(intent);
        }
    };
    /**
     * RecyclerView的Item中的Menu点击监听。
     */
    private OnItemMenuClickListener mItemMenuClickListener = new OnItemMenuClickListener() {
        @Override
        public void onItemClick(SwipeMenuBridge menuBridge, int position) {
            menuBridge.closeMenu();

           int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
           int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。

            if (direction==SwipeRecyclerView.RIGHT_DIRECTION){

                if (menuPosition==0) {
                       TimePickerDialog timePickerDialog= new TimePickerDialog(getActivity(),AlertDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
                           @Override
                           public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {

                               Calendar calendar=null;
                               try {
                                   calendar=DateUtil.changeStringToDate(datas.get(position).getPlanStartTime());
                               } catch (ParseException e) {
                                   e.printStackTrace();
                               }
                               long time=calendar.getTimeInMillis();
                               System.out.println("PersonFragment"+time);

                               long timeDifference=(hourOfDay*3600+minutes*60)*1000;
                               time=time-timeDifference;
                               if (time<System.currentTimeMillis()){
                                   Toast.makeText(getActivity(),"提醒时间比当前时间早了",Toast.LENGTH_SHORT).show();
                                   return;
                               }

                               Intent intentService = new Intent(getActivity(), AlarmService.class);
                               intentService.putExtra("event", datas.get(position).getEvent());
                               intentService.putExtra("time",time-DateUtil.getBootTime());
                               getActivity().startService(intentService);
                               Toast.makeText(getActivity(), "闹钟已提醒", Toast.LENGTH_LONG).show();
                           }
                       }, 0, 15, true);

                       timePickerDialog.setTitle("提前分钟提醒");
                          timePickerDialog.show();






                }else {
                    Intent intent=new Intent(getActivity(),AlarmDialogActivity.class);
                    intent.putExtra("event",datas.get(position));
                    startActivity(intent);
                }

            }else if (direction == SwipeRecyclerView.LEFT_DIRECTION){
                long id=datas.get(position).getId();
                datas.remove(position);
                fragmentPersonRecycleViewAdapter.notifyItemRemoved(position);

                Event event=new Event();
                event.setDone(true);
                event.setCompleteTime(DateUtil.changeDateToString(DateUtil.getNowDate()));
                //  System.out.println(datas.get(position).getDone()+" "+position+datas.get(position));

                event.update(id);
            }

//            if (direction == SwipeRecyclerView.RIGHT_DIRECTION) {
//                Toast.makeText(getContext(), "list第" + position + " asd"+datas.get(position)+"; 右侧菜单第" + menuPosition, Toast.LENGTH_SHORT).show();
//            } else if (direction == SwipeRecyclerView.LEFT_DIRECTION) {
//                Toast.makeText(getContext(), "list第" + position +  " asd"+datas.get(position)+"; 左侧菜单第" + menuPosition, Toast.LENGTH_SHORT).show();
//            }

        }
    };

    /**
     * 菜单创建器，在Item要创建菜单的时候调用。
     */
    private SwipeMenuCreator mSwipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int position) {


            // 1. MATCH_PARENT 自适应高度，保持和Item一样高;
            // 2. 指定具体的高，比如80;
            // 3. WRAP_CONTENT，自身高度，不推荐;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;

            SwipeMenuItem addItem = new SwipeMenuItem(getContext())
                    .setImage(R.mipmap.baseline_done_black_18dp)
                    .setWidth(150)
                    //.setBackgroundColor(Color.RED)
                    .setHeight(height);
            swipeLeftMenu.addMenuItem(addItem); // 添加菜单到左侧。

            SwipeMenuItem addAlarm = new SwipeMenuItem(getContext())
                    .setImage(R.mipmap.baseline_alarm_add_black_18dp)
                    //.setBackgroundColor(Color.RED)
                    .setWidth(150)
                    .setHeight(height);
            swipeRightMenu.addMenuItem(addAlarm);

            SwipeMenuItem workItem = new SwipeMenuItem(getContext())
                    .setImage(R.mipmap.baseline_hourglass_empty_black_18dp)
                   // .setBackgroundColor(Color.GREEN)
                    .setWidth(150)
                    .setHeight(height);


            swipeRightMenu.addMenuItem(workItem);

        }
    };

    void initEventData(){
        //步骤4：创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IPAdress) // 设置 网络请求 Url
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // 支持RxJava
                .build();

        // 步骤5：创建 网络请求接口 的实例
        final InternetRetrofit request = retrofit.create(InternetRetrofit.class);

        // 步骤6：采用Observable<...>形式 对 网络请求 进行封装
        final User user=new User();


        user.setId(Long.valueOf(contextUserId));
        //Observable<BaseResult> observable = request.getAllEvent(user);

        Observable<BaseResult> observableGeTAllEvent=request.getAllEvent(user);



        observableGeTAllEvent.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseResult value) {
                        String s=JSONObject.toJSONString(value);
                        JSONObject jsonObject=JSONObject.parseObject(s);
                        JSONArray jsonArray=jsonObject.getJSONArray("object");
                        String js=JSONObject.toJSONString(jsonArray);
                        List<Event>eventList=JSONObject.parseArray(js,Event.class);
                        List<Event>phoneEventList=LitePal.findAll(Event.class);
                        for (Event event:eventList){
                            boolean f=true;
                            for (Event eventPhone:phoneEventList){
                                if (event.getUuid().equals(eventPhone.getUuid())){
                                    f=false;
                                    break;
                                }
                            }
                            if (f){
                                event.save();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        List<Event> eventList = LitePal.where("userId = ?",contextUserId).find(Event.class);

        remoteData(eventList);

    }

    void remoteData(List<Event>eventList){

        System.out.println(JSONObject.toJSONString(eventList));


        //步骤4：创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IPAdress) // 设置 网络请求 Url
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // 支持RxJava
                .build();

        // 步骤5：创建 网络请求接口 的实例
        final InternetRetrofit request = retrofit.create(InternetRetrofit.class);
        Observable<BaseResult>observable=request.remote(eventList);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseResult value) {
//                       Toast.makeText(getActivity(),"更新成功",Toast.LENGTH_SHORT).show();
                        Toast.makeText(getActivity(),"上传数据成功",Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

}
