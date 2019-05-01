package com.example.qiubo.goaltracker.util;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.alamkanak.weekview.WeekViewDisplayable;
import com.example.qiubo.goaltracker.model.CalendarItem;
import com.example.qiubo.goaltracker.model.DO.Event;

import org.litepal.LitePal;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DataUtil {
   public static final String COLOR_NORMAL="#9AFF02";
    public static final String COLOR_IMPORTANT="#FFFF6F";
    public static final String COLOR_BUSY="#FF2D2D";
  @RequiresApi(api = Build.VERSION_CODES.N)
  public static List<WeekViewDisplayable<CalendarItem>> getDataList(Calendar startDate, Calendar endDate, Context context){
      SharedPreUtils sharedPreUtils=new SharedPreUtils(context);
      String userId= (String) SharedPreUtils.get("userId",null);
      String start=DateUtil.changeDateToString(startDate);
      String end=DateUtil.changeDateToString(endDate);
      List<WeekViewDisplayable<CalendarItem>> list = new ArrayList<>();
      List<Event> eventList = null;

      if (userId==null) {
         eventList = LitePal.where("done = ? and planStartTime > ? and planEndTime < ? and userId = ?", "0", start, end,"0").find(Event.class);
      }else {
          eventList = LitePal.where("done = ? and planStartTime > ? and planEndTime < ? and userId = ?", "0", start, end,userId).find(Event.class);
      }
      eventList=eventList.stream().sorted(Comparator.comparing(Event::getPlanStartTime)).collect(Collectors.toList());
      Calendar startTime = Calendar.getInstance();
      Calendar endTime = Calendar.getInstance();
      String color=COLOR_NORMAL;
      for (Event event : eventList) {

          try {
              startTime = DateUtil.changeStringToDate(event.getPlanStartTime());
              endTime = DateUtil.changeStringToDate(event.getPlanEndTime());
          } catch (ParseException e) {
              e.printStackTrace();
          }
          if ("1".equals(event.getLevel())){
              color=COLOR_NORMAL;
          }
          if ("2".equals(event.getLevel())){
              color=COLOR_IMPORTANT;
          }
          if ("3".equals(event.getLevel())){
              color=COLOR_BUSY;
          }
          CalendarItem weekEvent = new CalendarItem(event.getId(), "", startTime, endTime, event.getEvent(), Color.parseColor(color));
          list.add(weekEvent);
      }

      //LitePal.where("done = ？")
      return list;

  }
}
