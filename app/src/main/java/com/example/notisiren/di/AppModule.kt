package com.example.notisiren.di

import android.content.Context
import com.example.notisiren.data.AlarmControllerImpl
import com.example.notisiren.data.AlarmRepository
import com.example.notisiren.data.ListenerRepository
import com.example.notisiren.data.NotificationAccessCheckerImpl
import com.example.notisiren.domain.AlarmController
import com.example.notisiren.domain.NotificationAccessChecker
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAlarmRepository(): AlarmRepository = AlarmRepository

    @Provides
    @Singleton
    fun provideListenerRepository(): ListenerRepository = ListenerRepository

    @Provides
    @Singleton
    fun provideNotificationAccessChecker(@ApplicationContext app: Context): NotificationAccessChecker =
        NotificationAccessCheckerImpl(app)

    @Provides
    @Singleton
    fun provideAlarmController(@ApplicationContext app: Context): AlarmController =
        AlarmControllerImpl(app)


}