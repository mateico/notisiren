package com.example.notisiren.data

import android.content.ComponentName
import android.content.Context
import android.database.ContentObserver
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import com.example.notisiren.domain.NotificationAccessChecker
import com.example.notisiren.service.MyNotificationListener
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class NotificationAccessCheckerImpl @Inject constructor(
    @ApplicationContext private val appContext: Context
) : NotificationAccessChecker {

    override fun isEnabled(): Flow<Boolean> = callbackFlow {
        // Emit the current state initially
        trySend(checkAccess())

        // Observe changes in notification listener settings
        val observer = object : ContentObserver(Handler(Looper.getMainLooper())) {
            override fun onChange(selfChange: Boolean) {
                trySend(checkAccess())
            }
        }

        val uri = Settings.Secure.getUriFor("enabled_notification_listeners")
        appContext.contentResolver.registerContentObserver(uri, false, observer)

        awaitClose {
            appContext.contentResolver.unregisterContentObserver(observer)
        }
    }

    private fun checkAccess(): Boolean {
        val enabledListeners = Settings.Secure.getString(
            appContext.contentResolver,
            "enabled_notification_listeners"
        ) ?: return false

        val myComponent = ComponentName(appContext, MyNotificationListener::class.java)
        return enabledListeners.split(":")
            .mapNotNull { ComponentName.unflattenFromString(it) }
            .any { it.packageName == myComponent.packageName && it.className == myComponent.className }
    }
}