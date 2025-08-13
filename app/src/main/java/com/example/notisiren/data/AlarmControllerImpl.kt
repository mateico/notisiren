package com.example.notisiren.data

import android.content.Context
import com.example.notisiren.core.util.AlarmUtils
import com.example.notisiren.domain.AlarmController
import com.example.notisiren.domain.AlarmStatusRepository

class AlarmControllerImpl(
    private val appContext: Context,
    private val alarmStatusRepository: AlarmStatusRepository
) : AlarmController {

    override fun startAlarm() {
        AlarmUtils.startAlarm(appContext)
        alarmStatusRepository.setAlarming(true)
    }

    override fun stopAlarm() {
        AlarmUtils.stopAlarm()
        alarmStatusRepository.setAlarming(false)
    }
}