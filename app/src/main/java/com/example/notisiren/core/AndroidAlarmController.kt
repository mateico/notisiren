package com.example.notisiren.core

import android.content.Context
import com.example.notisiren.AlarmUtils

class AndroidAlarmController(
    private val appContext: Context
) : AlarmController {
    override fun startAlarm() = AlarmUtils.startAlarm(appContext)
    override fun stopAlarm() = AlarmUtils.stopAlarm()
}