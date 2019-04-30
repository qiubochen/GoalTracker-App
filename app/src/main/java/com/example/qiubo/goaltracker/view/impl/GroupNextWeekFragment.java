package com.example.qiubo.goaltracker.view.impl;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.qiubo.goaltracker.R;
import com.example.qiubo.goaltracker.model.WeekBarItem;
import com.example.qiubo.goaltracker.util.SharedPreUtils;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GroupNextWeekFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GroupNextWeekFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupNextWeekFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private BarChart barChart,lastWeekBarChart;

    private YAxis leftAxis;             //左侧Y轴
    private YAxis rightAxis;            //右侧Y轴
    private XAxis xAxis;                //X轴
    private Legend legend;              //图例
    private LimitLine limitLine;        //限制线
    private List<WeekBarItem>weekBarItemListNext,weekBarItemListLast;
    private String contextUserId;
    private OnFragmentInteractionListener mListener;

    public GroupNextWeekFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GroupNextWeekFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GroupNextWeekFragment newInstance(String param1, String param2) {
        GroupNextWeekFragment fragment = new GroupNextWeekFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_group_next_week, container, false);
        SharedPreUtils sharedPreUtils =new SharedPreUtils(getActivity());
        contextUserId= (String) SharedPreUtils.get("userId",null);
        barChart=view.findViewById(R.id.group_bar_chart_next_week);
        return view;
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
    public void showBarChart(List<WeekBarItem> dateValueList, String name, int color, BarChart barChartTemp) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < dateValueList.size(); i++) {
            /**
             * 此处还可传入Drawable对象 BarEntry(float x, float y, Drawable icon)
             * 即可设置柱状图顶部的 icon展示
             */
            BarEntry barEntry = new BarEntry(i, (float) dateValueList.get(i).getValue());
            entries.add(barEntry);
        }
        // 每一个BarDataSet代表一类柱状图
        BarDataSet barDataSet = new BarDataSet(entries, name);
        initBarDataSet(barDataSet, color);

//        // 添加多个BarDataSet时
//        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
//        dataSets.add(barDataSet);
//        BarData data = new BarData(dataSets);

        BarData data = new BarData(barDataSet);
        barChartTemp.setData(data);
    }
    /**
     * 柱状图始化设置 一个BarDataSet 代表一列柱状图
     *
     * @param barDataSet 柱状图
     * @param color      柱状图颜色
     */
    private void initBarDataSet(BarDataSet barDataSet, int color) {
        barDataSet.setColor(color);
        barDataSet.setFormLineWidth(1f);
        barDataSet.setFormSize(15.f);
        //不显示柱状图顶部值
        barDataSet.setDrawValues(false);
//        barDataSet.setValueTextSize(10f);
//        barDataSet.setValueTextColor(color);
    }
    /**
     * 初始化BarChart图表
     */
    private void initBarChartNext(BarChart barChart) {
        /***图表设置***/
        //背景颜色
        barChart.setBackgroundColor(Color.WHITE);
        //不显示图表网格
        barChart.setDrawGridBackground(false);
        //背景阴影
        barChart.setDrawBarShadow(false);
        barChart.setHighlightFullBarEnabled(false);
        //显示边框
        barChart.setDrawBorders(true);
        //设置动画效果
        barChart.animateY(1000, Easing.Linear);
        barChart.animateX(1000, Easing.Linear);

        /***XY轴的设置***/
        //X轴设置显示位置在底部
        xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        // xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);

        leftAxis = barChart.getAxisLeft();
        rightAxis = barChart.getAxisRight();
        //保证Y轴从0开始，不然会上移一点
//        leftAxis.setAxisMinimum(0f);
//        rightAxis.setAxisMinimum(0f);

        /***折线图例 标签 设置***/
        legend = barChart.getLegend();
        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextSize(11f);
        //显示位置
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        //是否绘制在图表里面
        legend.setDrawInside(false);
        //不显示图表边框
        barChart.setDrawBorders(false);
        //不显示右下角描述内容
        Description description = new Description();
        description.setEnabled(false);
        barChart.setDescription(description);
        //不显示X轴 Y轴线条
        xAxis.setDrawAxisLine(false);
        leftAxis.setDrawAxisLine(false);
        rightAxis.setDrawAxisLine(false);
        //不显示左侧Y轴
        leftAxis.setEnabled(false);
        //不显示X轴网格线
        xAxis.setDrawGridLines(false);
//右侧Y轴网格线设置为虚线
        rightAxis.enableGridDashedLine(10f, 10f, 0f);
        //X轴自定义值
        //X轴自定义值
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return weekBarItemListNext.get((int) value).getDay();
            }
        });
        rightAxis.setGranularity(1f);
        rightAxis.setAxisMinimum(0f);
        leftAxis.setAxisMinimum(0f);
    }

    /**
     * 初始化BarChart图表
     */
    private void initBarChartLast(BarChart barChart) {
        /***图表设置***/
        //背景颜色
        barChart.setBackgroundColor(Color.WHITE);
        //不显示图表网格
        barChart.setDrawGridBackground(false);
        //背景阴影
        barChart.setDrawBarShadow(false);
        barChart.setHighlightFullBarEnabled(false);
        //显示边框
        barChart.setDrawBorders(true);
        //设置动画效果
        barChart.animateY(1000, Easing.Linear);
        barChart.animateX(1000, Easing.Linear);

        /***XY轴的设置***/
        //X轴设置显示位置在底部
        xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        // xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);

        leftAxis = barChart.getAxisLeft();
        rightAxis = barChart.getAxisRight();
        //保证Y轴从0开始，不然会上移一点
//        leftAxis.setAxisMinimum(0f);
//        rightAxis.setAxisMinimum(0f);

        /***折线图例 标签 设置***/
        legend = barChart.getLegend();
        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextSize(11f);
        //显示位置
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        //是否绘制在图表里面
        legend.setDrawInside(false);
        //不显示图表边框
        barChart.setDrawBorders(false);
        //不显示右下角描述内容
        Description description = new Description();
        description.setEnabled(false);
        barChart.setDescription(description);
        //不显示X轴 Y轴线条
        xAxis.setDrawAxisLine(false);
        leftAxis.setDrawAxisLine(false);
        rightAxis.setDrawAxisLine(false);
        //不显示左侧Y轴
        leftAxis.setEnabled(false);
        //不显示X轴网格线
        xAxis.setDrawGridLines(false);
//右侧Y轴网格线设置为虚线
        rightAxis.enableGridDashedLine(10f, 10f, 0f);
        //X轴自定义值
        //X轴自定义值
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return weekBarItemListLast.get((int) value).getDay();
            }
        });
        rightAxis.setGranularity(1f);
        rightAxis.setAxisMinimum(0f);
        leftAxis.setAxisMinimum(0f);
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
}
