package com.example.notisiren.ui

import com.example.notisiren.MainDispatcherRule
import com.example.notisiren.domain.AlarmController
import com.example.notisiren.domain.NotificationAccessChecker
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

private class FakeAlarmController (val alarmRepo: FakeAlarmStatusRepository) : AlarmController {

    var isAlarming = false
        private set

    override fun startAlarm() {
        isAlarming = true
        alarmRepo.setAlarming(true)
    }

    override fun stopAlarm() {
        isAlarming = false
        alarmRepo.setAlarming(false)
    }
}

private class FakeNotificationAccessChecker(var value: Boolean) : NotificationAccessChecker {
    override fun isEnabled(): Boolean = value
}

@OptIn(ExperimentalCoroutinesApi::class)
class NotiSirenViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: NotiSirenViewModel
    private lateinit var alarmController: FakeAlarmController
    private lateinit var fakeNotificationAccessChecker: FakeNotificationAccessChecker
    private lateinit var alarmRepo: FakeAlarmStatusRepository
    private lateinit var notificationHelper: FakeNotificationHelper
    private lateinit var notificationListenerRepo: FakeNotificationListenerRepository

    @Before
    fun setUp() {
        alarmRepo = FakeAlarmStatusRepository()
        alarmController = FakeAlarmController(alarmRepo)
        fakeNotificationAccessChecker = FakeNotificationAccessChecker(true)
        notificationHelper = FakeNotificationHelper()
        notificationListenerRepo = FakeNotificationListenerRepository()
        viewModel = NotiSirenViewModel(
            alarmController,
            fakeNotificationAccessChecker,
            alarmRepo,
            notificationHelper,
            notificationListenerRepo)
    }

    @Test
    fun `refresh updates access flag`() = runTest {
        fakeNotificationAccessChecker.value = false
        viewModel.onEvent(NotiSirenEvent.RefreshAccess)
        advanceUntilIdle()
        assertThat(viewModel.state.value.notificationAccessEnabled).isFalse()
    }

    @Test
    fun `repo change reflects in state`() = runTest {
        alarmController.startAlarm()
        advanceUntilIdle()
        assertThat(viewModel.state.value.isAlarming).isTrue()
    }

    @Test
    fun `ClickStopAlarm stops and clears state`() = runTest {
        alarmController.startAlarm()
        advanceUntilIdle()

        assertThat(viewModel.state.value.isAlarming).isTrue()

        viewModel.onEvent(NotiSirenEvent.ClickStopAlarm)

        // let viewmodelScope/stateIn process the change
        advanceUntilIdle()

        assertThat(alarmController.isAlarming).isFalse()
        assertThat(viewModel.state.value.isAlarming).isFalse()
    }

}