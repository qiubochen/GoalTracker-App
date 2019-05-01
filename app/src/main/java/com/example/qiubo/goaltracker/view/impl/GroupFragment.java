
package com.example.qiubo.goaltracker.view.impl;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.qiubo.goaltracker.R;
import com.example.qiubo.goaltracker.model.DO.Event;
import com.example.qiubo.goaltracker.model.WeekBarItem;
import com.example.qiubo.goaltracker.util.DateUtil;
import com.example.qiubo.goaltracker.util.SharedPreUtils;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GroupFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GroupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupFragment extends Fragment {
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
    private PieChart mPieChart;
    private OnFragmentInteractionListener mListener;
    private TextView textView;

    public GroupFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GroupFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GroupFragment newInstance(String param1, String param2) {
        GroupFragment fragment = new GroupFragment();
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
        View view= inflater.inflate(R.layout.fragment_group, container, false);
        SharedPreUtils sharedPreUtils =new SharedPreUtils(getActivity());
        contextUserId= (String) SharedPreUtils.get("userId",null);
        barChart=view.findViewById(R.id.group_bar_chart_next_week);
        lastWeekBarChart=view.findViewById(R.id.group_bar_chart_last_week);
        mPieChart = view.findViewById(R.id.group_pie_chart);
        textView=view.findViewById(R.id.group_text);
        List<WeekBarItem> weekBarItemListNextWeek =initDataNextWeek();

        initBarChartNext(barChart);

        showBarChart(weekBarItemListNextWeek, "下周每日需要完成数量", getResources().getColor(R.color.colorPrimary),barChart);


        List<WeekBarItem> weekBarItemListLastWeek =initDataLastWeek();
        initBarChartLast(lastWeekBarChart);

        showBarChart(weekBarItemListLastWeek,"上周每日完成的数量",getResources().getColor(R.color.colorPrimary),lastWeekBarChart);
        initChart();
        initTextView();
        return view;
    }
    void initTextView(){
        int sum=LitePal.count(Event.class);
        int doneSum=LitePal.where("done = ?","1").count(Event.class);
        int notDoneSum=LitePal.where("done = ?","0").count(Event.class);
        String s="您一共创建了"+sum+"个任务,共完成了"+doneSum+"个任务，还有"+notDoneSum+"个任务未完成哦";
        textView.setText(s);
    }
    private List<WeekBarItem> initDataNextWeek(){
        weekBarItemListNext=new ArrayList<>();
        Calendar calendar;
        for (int i=1;i<=7;i++){
            calendar=DateUtil.getAfterDate(i);
            String stringData=DateUtil.changeDateToString(calendar);
            stringData=stringData.substring(0,8);
            WeekBarItem weekBarItem=new WeekBarItem();

            if (contextUserId==null) {
                weekBarItem.setValue(LitePal.where("planStartTime like ? and done = ? and userId = ?", stringData + "%", "0","0").count(Event.class));
            }else {
                weekBarItem.setValue(LitePal.where("planStartTime like ? and done = ? and userId = ?", stringData + "%", "0",contextUserId).count(Event.class));
            }
            stringData=stringData.substring(4,6)+"/"+stringData.substring(6,8);
            weekBarItem.setDay(stringData);
            weekBarItemListNext.add(weekBarItem);
        }
        return weekBarItemListNext;
    }
    private List<WeekBarItem> initDataLastWeek(){
        weekBarItemListLast=new ArrayList<>();
        Calendar calendar;
        for (int i=1;i<=7;i++){
            calendar=DateUtil.getAfterDate(i*-1);
            String stringData=DateUtil.changeDateToString(calendar);
            stringData=stringData.substring(0,8);
            WeekBarItem weekBarItem=new WeekBarItem();

            if (contextUserId==null) {
                weekBarItem.setValue(LitePal.where("completeTime like ? and done = ? and userId = ?", stringData + "%", "1","0").count(Event.class));
            }else {
                weekBarItem.setValue(LitePal.where("completeTime like ? and done = ? and userId = ?", stringData + "%", "1",contextUserId).count(Event.class));
            }
            stringData=stringData.substring(4,6)+"/"+stringData.substring(6,8);
            weekBarItem.setDay(stringData);
            weekBarItemListLast.add(weekBarItem);
        }
        return weekBarItemListLast;
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

    /**
     * 创建饼状图
     * @param
     */
    private void initChart(){

        //折现饼状图
        //mPieChart = view.findViewById(R.id.group_pie_chart);
        mPieChart.setUsePercentValues(true);
        mPieChart.getDescription().setEnabled(false);
        mPieChart.setExtraOffsets(5, 10, 5, 5);

        mPieChart.setEntryLabelColor(Color.BLACK);
        mPieChart.setDragDecelerationFrictionCoef(0.95f);
        //绘制中间文字
        mPieChart.setCenterText(generateCenterSpannableText());
        mPieChart.setExtraOffsets(20.f, 0.f, 20.f, 0.f);

        mPieChart.setDrawHoleEnabled(true);
        mPieChart.setHoleColor(Color.WHITE);

        mPieChart.setTransparentCircleColor(Color.WHITE);
        mPieChart.setTransparentCircleAlpha(110);

        mPieChart.setHoleRadius(58f);
        mPieChart.setTransparentCircleRadius(61f);

        mPieChart.setDrawCenterText(true);

        mPieChart.setRotationAngle(0);
        // 触摸旋转
        mPieChart.setRotationEnabled(true);
        mPieChart.setHighlightPerTapEnabled(true);


        // 添加一个选择监听器
        //mPieChart.setOnChartValueSelectedListener(this);

        List<Event>eventList=new ArrayList<>();

        int normal=LitePal.where("level = ? and done = ?","1","0").count(Event.class);
        int important=LitePal.where("level = ? and done = ? ","2","0").count(Event.class);
        int busy=LitePal.where("level = ? and done = ?","3","0").count(Event.class);
        //模拟数据
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        if (normal!=0) {
            entries.add(new PieEntry(normal, "一般"));
        }
        if (important!=0) {
            entries.add(new PieEntry(important, "重要"));
        }
        if (busy!=0) {
            entries.add(new PieEntry(busy, "紧急"));
        }

        //设置数据
        setData(entries,normal,important,busy);

        //默认动画
      //  mPieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        Legend l = mPieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(false);

    }
    //设置数据
    private void setData(ArrayList<PieEntry> entries,int normal,int important,int busy) {
        PieDataSet dataSet = new PieDataSet(entries, "重要性数据");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        //dataSet.setValueTextColor(Color.BLACK);
        ArrayList<Integer> colors = new ArrayList<Integer>();
        if (normal!=0) {
            colors.add(getResources().getColor(R.color.colorNormal));
        }
        if (important!=0) {
            colors.add(getResources().getColor(R.color.colorImportant));
        }
        if (busy!=0) {
            colors.add(getResources().getColor(R.color.colorBusy));
        }
//        for (int c : ColorTemplate.VORDIPLOM_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.JOYFUL_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.COLORFUL_COLORS)
//            colors.add(c);

//        for (int c : ColorTemplate.LIBERTY_COLORS)
//            colors.add(c);

//        for (int c : ColorTemplate.PASTEL_COLORS)
//            colors.add(c);

//        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);

        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.2f);
        dataSet.setValueLinePart2Length(0.4f);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        mPieChart.setData(data);

        // 撤销所有的亮点
        mPieChart.highlightValues(null);

        mPieChart.invalidate();
    }
    //绘制中心文字
    private CharSequence generateCenterSpannableText() {
        SpannableString s = new SpannableString("平时的任务重要性占比");
        //s.setSpan(new RelativeSizeSpan(1.5f), 0, 14, 0);
        //s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length() - 15, 0);
        //s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);
        //s.setSpan(new RelativeSizeSpan(.65f), 14, s.length() - 15, 0);
        //s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
        //s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length()-17, s.length(), 0);
        return s;
    }


    public void showBarChart(List<WeekBarItem> dateValueList, String name, int color,BarChart barChartTemp) {
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
