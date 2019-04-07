package com.example.qiubo.goaltracker.view.impl;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.Toast;


import com.example.qiubo.goaltracker.R;
import com.example.qiubo.goaltracker.adapter.FragmentPersonRecycleViewAdapter;
import com.example.qiubo.goaltracker.model.DO.Event;
import com.example.qiubo.goaltracker.service.AlarmService;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.yanzhenjie.recyclerview.OnItemClickListener;
import com.yanzhenjie.recyclerview.OnItemMenuClickListener;
import com.yanzhenjie.recyclerview.SwipeMenu;
import com.yanzhenjie.recyclerview.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import org.litepal.LitePal;

import java.util.List;

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
        //userImageView=view.findViewById(R.id.person_user);
        //userImageView.setOnClickListener(this);
        toolbar=view.findViewById(R.id.fragment_person_toolbar);
        swipeRecyclerView=view.findViewById(R.id.fragment_person_recycle_view);
        swipeRefreshLayout=view.findViewById(R.id.person_refresh_layout);
        navigationView=view.findViewById(R.id.person_navigation_view);
        drawerLayout=view.findViewById(R.id.person_drawer_layout);
        searchView = (MaterialSearchView) view.findViewById(R.id.fragment_person_search_view);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                datas.clear();
                datas.addAll(LitePal.where("event like ? and done = ?","%"+query+"%","0").find(Event.class));
                loadData();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                datas.clear();
                datas.addAll(LitePal.where("event like ? and done = ?","%"+newText+"%","0").find(Event.class));
                loadData();
                return true;
            }
        });
        initToolbar();
        initNavigaitonView();
        return view;
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
//            case R.id.person_user:{
//                Intent intent=new Intent(getActivity(),LoginActivity.class);
//                startActivity(intent);
//            };break;
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
//    private void setSearchView(MaterialSearchView materialSearchView){
//        materialSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//
//                return true;
//            }
//        });
//    }
    private  void initToolbar() {
        toolbar.setTitle("");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
    }

    /**
     * 创建recycleview
     */
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
    private List<Event> initdata(){
        List<Event> datasTemp=LitePal.where("done = ?","0").find(Event.class);

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
        @Override
        public void onRefresh() {
            datas.clear();
            datas.addAll(initdata());
           loadData();
        }
    };
    /**
     * RecyclerView的Item点击监听。
     */
    private OnItemClickListener mItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(View itemView, int position) {
            Toast.makeText(getContext(), "第" + position + "个", Toast.LENGTH_SHORT).show();
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
//            int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。

            if (direction==SwipeRecyclerView.RIGHT_DIRECTION){
                Intent intentService=new Intent(getActivity(),AlarmService.class);
                intentService.putExtra("event",datas.get(position).getEvent());
                getActivity().startService(intentService);
                Toast.makeText(getActivity(),"闹钟已提醒",Toast.LENGTH_LONG).show();

            }else if (direction == SwipeRecyclerView.LEFT_DIRECTION){
                long id=datas.get(position).getId();
                datas.remove(position);
                fragmentPersonRecycleViewAdapter.notifyItemRemoved(position);

                Event event=new Event();
                event.setDone(true);
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
                    .setWidth(120)
                    .setHeight(height);
            swipeLeftMenu.addMenuItem(addItem); // 添加菜单到左侧。

            SwipeMenuItem addAlarm = new SwipeMenuItem(getContext())
                    .setImage(R.mipmap.baseline_alarm_add_black_18dp)
                    .setWidth(120)
                    .setHeight(height);
            swipeRightMenu.addMenuItem(addAlarm);

        }
    };
}
