package com.example.notisiren.data

import android.content.Context
import com.example.notisiren.core.util.AlarmUtils
import com.example.notisiren.domain.AlarmController

class AlarmControllerImpl(
    private val appContext: Context
) : AlarmController {
    override fun startAlarm() = AlarmUtils.startAlarm(appContext)
    override fun stopAlarm() = AlarmUtils.stopAlarm()
}