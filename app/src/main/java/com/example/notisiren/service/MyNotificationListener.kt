package com.example.notisiren.service

import android.content.ComponentName
import android.content.Context
import android.database.ContentObserver
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.example.notisiren.core.util.AlarmUtils
import com.example.notisiren.data.AlarmRepository
import com.example.notisiren.data.ListenerRepository

class MyNotificationListener : NotificationListenerService() {

    @Volatile private var isBoundBySystem: Boolean = false
    private var settingsObserver: ContentObserver? = null

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Service created")
        registerEnabledObserver()
        publishListeningState()
    }

    override fun onDestroy() {
        unregisterEnabledObserver()
        Log.d(TAG, "Service destroyed")
        super.onDestroy()
    }

    override fun onListenerConnected() {
        super.onListenerConnected()
        isBoundBySystem = true
        publishListeningState()
        Log.d(TAG, "onListenerConnected()")
    }

    override fun onListenerDisconnected() {
        super.onListenerDisconnected()
        isBoundBySystem = false
        publishListeningState()
        Log.d(TAG, "onListenerDisconnected()")
        requestRebind(ComponentName(this, MyNotificationListener::class.java))
    }

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

        Log.d(TAG, "[$packageName] Title: $title | Text: $text")
    }

    // --- Enable-in-settings + bound state helper ---

    private fun registerEnabledObserver() {
        val uri = Settings.Secure.getUriFor(KEY_ENABLED_LISTENERS)
        settingsObserver = object : ContentObserver(Handler(Looper.getMainLooper())) {
            override fun onChange(selfChange: Boolean) {
                publishListeningState()
            }
        }.also { contentResolver.registerContentObserver(uri, false, it) }
    }

    private fun unregisterEnabledObserver() {
        settingsObserver?.let { contentResolver.unregisterContentObserver(it) }
        settingsObserver = null
    }

    private fun publishListeningState() {
        val enabled = isUserEnabledInSettings(this)
        ListenerRepository.setListening(enabled)
    }

    fun isUserEnabledInSettings(context: Context): Boolean {
        val flat = Settings.Secure.getString(
            context.contentResolver,
            KEY_ENABLED_LISTENERS
        ) ?: return false

        val my = ComponentName(context, MyNotificationListener::class.java)
        return flat.split(":")
            .mapNotNull { ComponentName.unflattenFromString(it) }
            .any { it.packageName == my.packageName && it.className == my.className }
    }

    companion object {
        private const val TAG = "NotificationService"
        private const val KEY_ENABLED_LISTENERS = "enabled_notification_listeners"
    }
}