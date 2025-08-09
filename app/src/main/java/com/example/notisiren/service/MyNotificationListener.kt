package com.example.notisiren.service

import android.content.ComponentName
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.example.notisiren.core.util.AlarmUtils
import com.example.notisiren.data.AlarmRepository

class MyNotificationListener : NotificationListenerService() {

    override fun onNotificationPosted(sbn: StatusBarNotification) {
       val packageName = sbn.packageName
       if (packageName != "com.google.android.gm") return

       val extras = sbn.notification.extras
       val title = extras.getString("android.title") ?: ""
       val text = extras.getString("android.text") ?: ""

       if (title.contains("me", true) || text.contains("urgent", true)) {
           AlarmUtils.startAlarm(this)
           AlarmRepository.setAlarming(true)
       }

       Log.d("NotificationCheck", "[$packageName] Title: $title | Text: $text")
   }

    override fun onListenerDisconnected() {
        super.onListenerDisconnected()
        Log.e("NotificationService", "Listener disconnected")
        requestRebind(ComponentName(this, MyNotificationListener::class.java))
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("NotificationService", "NotificationListenerService created")
    }
}