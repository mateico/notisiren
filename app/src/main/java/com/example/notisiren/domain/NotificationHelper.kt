package com.example.notisiren.domain

import android.app.PendingIntent

interface NotificationHelper {
    fun createNotificationChannel()
    fun createStopAlarmPendingIntent(): PendingIntent
    fun showAlarmNotification(title: String, content: String)
}