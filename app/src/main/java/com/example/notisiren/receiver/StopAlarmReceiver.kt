package com.example.notisiren.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.notisiren.di.BroadcastReceiverEntryPoint
import dagger.hilt.android.EntryPointAccessors

class StopAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null) return

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(1234)

        val entryPoint = EntryPointAccessors.fromApplication(
        context.applicationContext,
        BroadcastReceiverEntryPoint::class.java
        )

        val alarmController = entryPoint.alarmController()
        alarmController.stopAlarm()
    }
}
