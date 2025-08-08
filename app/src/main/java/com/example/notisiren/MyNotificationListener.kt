package com.example.notisiren

import android.content.ComponentName
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

class MyNotificationListener : NotificationListenerService() {

    override fun onNotificationPosted(sbn: StatusBarNotification) {
       val packageName = sbn.packageName
       if (packageName != "com.google.android.gm") return

       val extras = sbn.notification.extras
       val title = extras.getString("android.title") ?: ""
       val text = extras.getString("android.text") ?: ""

       if (title.contains("urgent", true) || text.contains("mateo.rial@gmail.com", true)) {
           AlarmUtils.startAlarm(this)
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

    /*fun isNotificationServiceEnabled(context: Context): Boolean {
        val cn = ComponentName(context, MyNotificationListener::class.java)
        val flat = Settings.Secure.getString(context.contentResolver, "enabled_notification_listeners")
        return flat?.contains(cn.flattenToString()) == true
    }*/
}
