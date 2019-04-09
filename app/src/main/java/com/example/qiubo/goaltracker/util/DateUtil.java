package com.example.qiubo.goaltracker.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    public static Calendar getNowDate(){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar;
    }
    public static Calendar getAfterDate(int day){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE,day);
        return calendar;
    }
    public static Calendar changeStringToDate(String TimeString) throws ParseException {
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat sdf= new SimpleDateFormat("yyyyMMddHHmm");
        Date date=sdf.parse(TimeString);

        calendar.setTime(date);
        System.out.println("----"+calendar.getTime());
        return calendar;
    }
    public static String changeDateToString(Calendar calendar){
        SimpleDateFormat sdf= new SimpleDateFormat("yyyyMMddHHmm");
        String result=sdf.format(calendar.getTime());
        return result;
    }
}

