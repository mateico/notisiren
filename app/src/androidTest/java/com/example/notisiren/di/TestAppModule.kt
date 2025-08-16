package com.example.notisiren.di

import com.example.notisiren.domain.AlarmController
import com.example.notisiren.domain.AlarmStatusRepository
import com.example.notisiren.domain.NotificationAccessChecker
import com.example.notisiren.domain.NotificationHelper
import com.example.notisiren.domain.NotificationListenerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModule::class]
)
object TestAppModule {

    @Provides @Singleton
    fun provideFakeAccessChecker(): FakeAccessChecker = FakeAccessChecker()

    // Bind the interface to the same fake instance used by the test field
    @Provides @Singleton
    fun provideNotificationAccessChecker(fake: FakeAccessChecker): NotificationAccessChecker = fake

    @Provides @Singleton
    fun provideAlarmStatusRepository(): AlarmStatusRepository = FakeAlarmStatusRepository()

    @Provides
    @Singleton
    fun provideNotificationListenerRepository(): NotificationListenerRepository = FakeNotificationListenerRepository()

    @Provides @Singleton
    fun provideAlarmController(): AlarmController = FakeAlarmController()

    @Provides @Singleton
    fun provideNotificationHelper(): NotificationHelper = FakeNotificationHelper()
}


