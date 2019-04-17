package com.example.qiubo.goaltracker.model;

public class WeekBarItem {
    private String day;
    private double value=0;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
