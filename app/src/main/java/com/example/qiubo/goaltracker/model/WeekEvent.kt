package com.example.qiubo.goaltracker.model

import com.alamkanak.weekview.WeekViewDisplayable
import com.alamkanak.weekview.WeekViewEvent
import java.util.*

class WeekEvent(
        val id: Long,
        val title: String,
        val startTime: Calendar,
        val endTime: Calendar,
        val location: String,
        val color: Int,
        val isAllDay: Boolean
) : WeekViewDisplayable<WeekEvent> {

    override fun toWeekViewEvent(): WeekViewEvent<WeekEvent> {
        return WeekViewEvent(id, title, startTime, endTime, location, color, isAllDay, this)
    }

}