package com.example.notisiren.data

import android.content.ComponentName
import android.content.Context
import android.provider.Settings
import com.example.notisiren.domain.NotificationAccessChecker
import com.example.notisiren.service.MyNotificationListener
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject

class NotificationAccessCheckerImpl @Inject constructor(
    @param:ApplicationContext private val appContext: Context
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