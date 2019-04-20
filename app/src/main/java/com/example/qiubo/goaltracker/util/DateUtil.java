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

    public static Calendar changeStringToYMD(String TimeString) throws ParseException {
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat sdf= new SimpleDateFormat("yyyyMMdd");
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
    public static String getItemTime(String startTime,String endTime){
        String result="";
        int startYear,startMonth,startDay,startHour,startMinute,endYear,endMonth,endDay,endHour,endMinute;
        startYear=Integer.valueOf(startTime.substring(0,4));
        startMonth=Integer.valueOf(startTime.substring(4,6));
        startDay=Integer.valueOf(startTime.substring(6,8));
        startHour=Integer.valueOf(startTime.substring(8,10));
        startMinute=Integer.valueOf(startTime.substring(10,startTime.length()));
        endYear=Integer.valueOf(endTime.substring(0,4));
        endMonth=Integer.valueOf(endTime.substring(4,6));
        endDay=Integer.valueOf(endTime.substring(6,8));
        endHour=Integer.valueOf(endTime.substring(8,10));
        endMinute=Integer.valueOf(endTime.substring(10,startTime.length()));

        if (startYear==getNowDate().get(Calendar.YEAR)&&startMonth==getNowDate().get(Calendar.MONTH)+1&&startDay==getNowDate().get(Calendar.DATE)){
            result="今天 "+startHour+":"+startMinute+"-"+endHour+":"+endMinute;
            return result;
        }else {
            if (startYear==getNowDate().get(Calendar.YEAR)&&startMonth==getNowDate().get(Calendar.MONTH)+1&&startDay==getAfterDate(-1).get(Calendar.DATE)){
                result="昨天 "+startHour+":"+startMinute+"-"+endHour+":"+endMinute;
                return result;
            }
            if (startYear == getNowDate().get(Calendar.YEAR) && startMonth == getNowDate().get(Calendar.MONTH)+1 && startDay == getAfterDate(1).get(Calendar.DATE)) {
                result = "明天 " + startHour + ":" + startMinute + "-" + endHour + ":" + endMinute;
                return result;
            }
            if (startYear != getNowDate().get(Calendar.YEAR) ) {
                result  = startYear+" "+startMonth+"/"+startDay+" "+startHour+":"+startMinute+"-"+endHour+":"+endMinute;
                return result;
            }else {
                result  = startMonth+"/"+startDay+" "+startHour+":"+startMinute+"-"+endHour+":"+endMinute;
                return result;
            }

        }



    }
}

