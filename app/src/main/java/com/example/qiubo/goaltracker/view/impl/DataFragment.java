package com.example.qiubo.goaltracker.view.impl;

import android.content.Context;

import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;



import com.alamkanak.weekview.MonthChangeListener;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewDisplayable;
import com.example.qiubo.goaltracker.R;
import com.example.qiubo.goaltracker.model.CalendarItem;

import com.example.qiubo.goaltracker.util.DataUtil;
import com.example.qiubo.goaltracker.util.DateUtil;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DataFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DataFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DataFragment extends Fragment  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private WeekView<CalendarItem> weekView;
    private Toolbar toolbar;

    public DataFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DataFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DataFragment newInstance(String param1, String param2) {
        DataFragment fragment = new DataFragment();
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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_data, container, false);
        weekView=view.findViewById(R.id.weekView);
        weekView.setEventTextColor(Color.BLACK);
        toolbar=view.findViewById(R.id.data_toolbar);
        if (weekView!=null){
            weekView.setMonthChangeListener(new MonthChangeListener<CalendarItem>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @NotNull
                @Override
                public List<WeekViewDisplayable<CalendarItem>> onMonthChange(Calendar calendar, Calendar calendar1) {
                  return DataUtil.getDataList(calendar,calendar1,getActivity());
                }

            });

        }


        System.out.println("+++++++++++++");
        weekView.setMinDate(DateUtil.getNowDate());
        System.out.println(DateUtil.getNowDate().getTime());
        weekView.setMaxDate(DateUtil.getAfterDate(6));
        System.out.println(DateUtil.getAfterDate(5).getTime());
        weekView.setNumberOfVisibleDays(5);

        initToolbar();

        return view;
    }
    private  void initToolbar() {
        toolbar.inflateMenu(R.menu.day_toolbar_items);
        toolbar.setTitle("日程表");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id=menuItem.getItemId();
                switch (id){
                    case R.id.day_three:{
                        weekView.setNumberOfVisibleDays(3);
                    };break;
                    case R.id.day_five:{
                        weekView.setNumberOfVisibleDays(5);
                    };break;
                    case R.id.day_seven:{
                        weekView.setNumberOfVisibleDays(7);
                    };break;
                }
                return true;
            }
        });
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.day_toolbar_items, menu);

    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id=item.getItemId();
//        switch (id){
//            case R.id.day_three:{
//                weekView.setNumberOfVisibleDays(3);
//            };break;
//            case R.id.day_five:{
//                weekView.setNumberOfVisibleDays(5);
//            };break;
//            case R.id.day_seven:{
//                weekView.setNumberOfVisibleDays(7);
//            };break;
//        }
//        return super.onOptionsItemSelected(item);
//    }

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
}
