package com.example.qiubo.goaltracker.util;

import android.graphics.Color;

import com.alamkanak.weekview.WeekViewDisplayable;
import com.example.qiubo.goaltracker.model.CalendarItem;
import com.example.qiubo.goaltracker.model.DO.Event;

import org.litepal.LitePal;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DataUtil {
   public static final String COLOR_GREEN="#00DB00";
    public static final String COLOR_RED="#D81B60";
  public static List<WeekViewDisplayable<CalendarItem>> getDataList(Calendar startDate,Calendar endDate){
      String start=DateUtil.changeDateToString(startDate);
      String end=DateUtil.changeDateToString(endDate);
      List<WeekViewDisplayable<CalendarItem>> list = new ArrayList<>();
      List<Event> eventList = LitePal.where("done = ? and planStartTime > ? and planEndTime < ?", "0",start,end).find(Event.class);
      Calendar startTime = Calendar.getInstance();
      Calendar endTime = Calendar.getInstance();
      String color=COLOR_GREEN;
      for (Event event : eventList) {

          try {
              startTime = DateUtil.changeStringToDate(event.getPlanStartTime());
              endTime = DateUtil.changeStringToDate(event.getPlanEndTime());
          } catch (ParseException e) {
              e.printStackTrace();
          }
          if (color.equals(COLOR_GREEN)){
              color=COLOR_RED;
          }else {
              color=COLOR_GREEN;
          }
          CalendarItem weekEvent = new CalendarItem(event.getId(), "", startTime, endTime, event.getEvent(), Color.parseColor(color));
          list.add(weekEvent);
      }

      //LitePal.where("done = ？")
      return list;

  }
}
