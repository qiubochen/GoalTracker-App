package com.example.qiubo.goaltracker.model;

import com.alamkanak.weekview.WeekViewDisplayable;
import com.alamkanak.weekview.WeekViewEvent;

import java.util.Calendar;

public class CalendarItem implements WeekViewDisplayable<CalendarItem> {

    private long id;
    private String title;
    private Calendar startTime;
    private Calendar endTime;
    private String location;
    private int color;

    public CalendarItem(long id, String title, Calendar startTime, Calendar endTime, String location, int color) {
        this.id = id;
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.color = color;
    }
    /* ... */

    @Override
    public WeekViewEvent<CalendarItem> toWeekViewEvent() {
        // Note: It's important to pass "this" as the last argument to WeekViewEvent's constructor.
        // This way, the EventClickListener can return this object in its onEventClick() method.
        boolean isAllDay = false;
        return new WeekViewEvent<CalendarItem>(
                id, title, startTime,
                endTime, location, color, isAllDay, this
        );
    }

}