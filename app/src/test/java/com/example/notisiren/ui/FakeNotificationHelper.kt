package com.example.notisiren.ui

import android.app.PendingIntent
import com.example.notisiren.domain.NotificationHelper

class FakeNotificationHelper : NotificationHelper {
    override fun createNotificationChannel() {
        TODO("Not yet implemented")
    }

    override fun createStopAlarmPendingIntent(): PendingIntent {
        TODO("Not yet implemented")
    }

    override fun showAlarmNotification(title: String, content: String) {
        TODO("Not yet implemented")
    }
}