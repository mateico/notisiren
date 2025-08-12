package com.example.notisiren.domain

import kotlinx.coroutines.flow.Flow

interface NotificationListenerRepository {

    val isListening: Flow<Boolean>

    fun setIsListening(value: Boolean)

}