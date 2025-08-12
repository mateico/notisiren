package com.example.notisiren.domain

import kotlinx.coroutines.flow.Flow

interface AlarmStatusRepository {
    val isAlarming: Flow<Boolean>
    fun setAlarming(value: Boolean)
}