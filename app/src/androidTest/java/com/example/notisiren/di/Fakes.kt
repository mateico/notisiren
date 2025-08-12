package com.example.notisiren.di

import com.example.notisiren.domain.*
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
    var enabled = true
    override fun isEnabled(): Boolean = enabled
}

class FakeAlarmController : AlarmController {
    var isAlarming = false
        private set
    override fun startAlarm() { isAlarming = true }
    override fun stopAlarm()  { isAlarming = false }
}