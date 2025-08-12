package com.example.notisiren.data

import com.example.notisiren.domain.NotificationListenerRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NotificationListenerRepositoryImpl @Inject constructor() : NotificationListenerRepository {

    private val _isListening = MutableStateFlow(false)
    override val isListening = _isListening.asStateFlow()

    override fun setIsListening(value: Boolean) {
        _isListening.value = value
    }
}