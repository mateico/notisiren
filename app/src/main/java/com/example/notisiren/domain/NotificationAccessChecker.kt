package com.example.notisiren.domain

import kotlinx.coroutines.flow.Flow

interface NotificationAccessChecker {
    fun isEnabled(): Flow<Boolean>
}