package com.example.notisiren.ui

import com.example.notisiren.MainDispatcherRule
import com.example.notisiren.domain.AlarmController
import com.example.notisiren.domain.NotificationAccessChecker
import com.example.notisiren.data.AlarmRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

private class FakeAlarm : AlarmController {

    var isAlarming = false
        private set

    override fun startAlarm() { isAlarming = true }
    override fun stopAlarm() { isAlarming = false }
}

private class FakeAccess(var value: Boolean) : NotificationAccessChecker {
    override fun isEnabled(): Boolean = value
}

@OptIn(ExperimentalCoroutinesApi::class)
class NotiSirenViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: NotiSirenViewModel
    private lateinit var alarm: FakeAlarm
    private lateinit var access: FakeAccess

    @Before
    fun setUp() {
        AlarmRepository.setAlarming(false)
        alarm = FakeAlarm()
        access = FakeAccess(true)
        viewModel = NotiSirenViewModel(alarm, access)
    }

    @Test
    fun `refresh updates access flag`() = runTest {
        access.value = false
        viewModel.onEvent(NotiSirenEvent.RefreshAccess)
        advanceUntilIdle()
        assertThat(viewModel.state.value.notificationAccessEnabled).isFalse()
    }

    @Test
    fun `repo change reflects in state`() = runTest {
        AlarmRepository.setAlarming(true)
        advanceUntilIdle()
        assertThat(viewModel.state.value.isAlarming).isTrue()
    }

    @Test
    fun `ClickStopAlarm stops and clears state`() = runTest {
        AlarmRepository.setAlarming(true)

        advanceUntilIdle()

        assertThat(viewModel.state.value.isAlarming).isTrue()

        viewModel.onEvent(NotiSirenEvent.ClickStopAlarm)

        assertThat(alarm.isAlarming).isFalse()
        assertThat(viewModel.state.value.isAlarming).isFalse()
    }

}