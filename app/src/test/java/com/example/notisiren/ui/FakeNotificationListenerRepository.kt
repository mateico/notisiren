package com.example.notisiren.ui

import com.example.notisiren.domain.NotificationListenerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeNotificationListenerRepository : NotificationListenerRepository {

    private val _isListening = MutableStateFlow(false)
    override val isListening = _isListening.asStateFlow()

    override fun setIsListening(value: Boolean) { _isListening.value = value}

}

