package com.example.qiubo.goaltracker.util;

public class TextChange {
    public static String mergeTime(String tag,String time){
        String result=new String();
        String[] splitTag=tag.split(" ");
        result=splitTag[2];
        switch (splitTag[0]){
            case "Jan":{
                result=result+"01";
            };break;
            case "Feb":{
                result=result+"02";
            };break;
            case "Mar":{
                result=result+"03";
            };break;
            case "Apr":{
                result=result+"04";
            };break;
            case "May":{
                result=result+"05";
            };break;
            case "Jun":{
                result=result+"06";
            };break;
            case "Jul":{
                result=result+"07";
            };break;
            case "Aug":{
                result=result+"08";
            };break;
            case "Sep":{
                result=result+"09";
            };break;
            case "Oct":{
                result=result+"10";
            };break;
            case "Nov":{
                result=result+"11";
            };break; case "Dec":{
                result=result+"12";
            };break;

        }
        result+=splitTag[1];
        String[] splitTime=time.split(":");
        result+=splitTime[0];
        result+=splitTime[1];
        return result;
    }

    public static String setTime(String time){
        String result;
        result=time.substring(8,10);
        result = result+":";
        result+=time.substring(10,time.length());
        return result;
    }

    public static String changeTagShow(String data){
        String result;
        result=data.substring(0,4);
        result+="年";
        result+=data.substring(4,6);
        result+="月";
        result+=data.substring(6,8);
        result+="日";
        return result;
    }
}
