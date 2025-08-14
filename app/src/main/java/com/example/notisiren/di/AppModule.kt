package com.example.notisiren.di

import com.example.notisiren.data.AlarmControllerImpl
import com.example.notisiren.data.AlarmStatusRepositoryImpl
import com.example.notisiren.data.NotificationAccessCheckerImpl
import com.example.notisiren.data.NotificationListenerRepositoryImpl
import com.example.notisiren.domain.AlarmController
import com.example.notisiren.domain.AlarmStatusRepository
import com.example.notisiren.domain.NotificationAccessChecker
import com.example.notisiren.domain.NotificationListenerRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindAlarmStatusRepository(
        impl: AlarmStatusRepositoryImpl
    ): AlarmStatusRepository

    @Binds
    @Singleton
    abstract fun bindNotificationListenerRepository(
        impl: NotificationListenerRepositoryImpl
    ): NotificationListenerRepository

    @Binds
    @Singleton
    abstract fun bindAlarmController(
        impl: AlarmControllerImpl
    ): AlarmController

    @Binds
    @Singleton
    abstract fun bindNotificationAccessChecker(
        impl: NotificationAccessCheckerImpl
    ): NotificationAccessChecker
}