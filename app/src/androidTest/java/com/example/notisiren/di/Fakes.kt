package com.example.notisiren.di

import android.app.PendingIntent
import com.example.notisiren.domain.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeAlarmStatusRepository : AlarmStatusRepository {
    private val _isAlarming = MutableStateFlow(false)
    override val isAlarming = _isAlarming.asStateFlow()
    override fun setAlarming(value: Boolean) { _isAlarming.value = value }
}

class FakeNotificationListenerRepository : NotificationListenerRepository {
    private val _isListening = MutableStateFlow(false)
    override val isListening = _isListening.asStateFlow()
    override fun setIsListening(value: Boolean) { _isListening.value = value }
}

class FakeAccessChecker : NotificationAccessChecker {
    private val _accessEnabledFlow = MutableStateFlow(false)
    override fun isEnabled(): Flow<Boolean> = _accessEnabledFlow.asStateFlow()

    fun setAccessEnabled(hasAccess: Boolean) {
        _accessEnabledFlow.value = hasAccess
    }
}

class FakeAlarmController : AlarmController {
    var isAlarming = false
        private set
    override fun startAlarm() { isAlarming = true }
    override fun stopAlarm()  { isAlarming = false }
}

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