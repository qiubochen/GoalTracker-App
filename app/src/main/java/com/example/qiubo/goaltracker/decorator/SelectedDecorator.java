package com.example.qiubo.goaltracker.decorator;

import android.graphics.Color;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.Date;
import java.util.List;

public class SelectedDecorator implements DayViewDecorator {
    private List<CalendarDay> calendarDayList;

    public SelectedDecorator(List<CalendarDay> calendarDayList) {
        this.calendarDayList=calendarDayList;
    }
    @Override
    public boolean shouldDecorate(CalendarDay calendarDay) {
        return calendarDayList.contains(calendarDay);
    }

    @Override
    public void decorate(DayViewFacade dayViewFacade) {
      dayViewFacade.addSpan(new DotSpan(5, Color.RED));
    }
}
