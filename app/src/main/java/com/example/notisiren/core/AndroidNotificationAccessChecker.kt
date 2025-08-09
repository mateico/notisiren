package com.example.notisiren.core

import android.content.ComponentName
import android.content.Context
import android.provider.Settings
import com.example.notisiren.MyNotificationListener
import kotlin.jvm.java

class AndroidNotificationAccessChecker(
    private val appContext: Context
): NotificationAccessChecker {
    override fun isEnabled(): Boolean {
        val component = ComponentName(appContext, MyNotificationListener::class.java)
        val enabled = Settings.Secure.getString(
            appContext.contentResolver,
            "enabled_notification_listeners"
        )
        return enabled?.contains(component.flattenToString()) == true
    }
}