package com.example.notisiren.di

import androidx.compose.ui.test.isEnabled
import com.example.notisiren.domain.AlarmController
import com.example.notisiren.domain.NotificationAccessChecker
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
    fun provideAlarmController(): AlarmController = FakeAlarmController()

    @Provides @Singleton
    fun provideFakeAccessChecker(): FakeAccessChecker = FakeAccessChecker().apply {
        enabled = true
    }

    @Provides @Singleton
    fun provideNotificationAccessChecker(fake: FakeAccessChecker): NotificationAccessChecker = fake
}

class FakeAlarmController : AlarmController {
    var isAlarming = false
        private set

    override fun startAlarm() {
        isAlarming = true
    }

    override fun stopAlarm() {
        isAlarming = false
    }
}

class FakeAccessChecker : NotificationAccessChecker {
    var enabled = true

    override fun isEnabled(): Boolean = enabled
}