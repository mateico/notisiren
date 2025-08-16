package com.example.notisiren.di

import com.example.notisiren.domain.AlarmStatusRepository
import com.example.notisiren.domain.NotificationHelper
import com.example.notisiren.domain.NotificationListenerRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface ServiceEntryPoint {
    fun alarmStatusRepository(): AlarmStatusRepository
    fun notificationListenerRepository(): NotificationListenerRepository
    fun notificationHelper(): NotificationHelper
}