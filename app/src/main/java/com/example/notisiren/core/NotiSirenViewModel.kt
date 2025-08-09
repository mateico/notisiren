package com.example.notisiren.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notisiren.ui.NotiSirenEffect
import com.example.notisiren.ui.NotiSirenEvent
import com.example.notisiren.ui.NotiSirenState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class NotiSirenViewModel(
    private val alarm: AlarmController,
    private val access: NotificationAccessChecker
) : ViewModel() {

    private val _state = MutableStateFlow(NotiSirenState())
    val state: StateFlow<NotiSirenState> = _state.asStateFlow()

    private val _effect = Channel<NotiSirenEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        refreshAccess()
    }

    fun onEvent(event: NotiSirenEvent) {
        when (event) {
            NotiSirenEvent.ClickEnableNotification ->
                viewModelScope.launch {_effect.send(NotiSirenEffect.OpenNotificationAccessSettings)}
            NotiSirenEvent.ClickStopAlarm -> stopAlarm()
            NotiSirenEvent.RefreshAccess -> refreshAccess()
        }
    }

    private fun refreshAccess() {
        _state.value = _state.value.copy(isLoading = true)
        val enabled = access.isEnabled()
        _state.value = _state.value.copy(
            notificationAccessEnabled = enabled,
            isLoading = false
        )
    }

    private fun stopAlarm() {
        runCatching { alarm.stopAlarm() }
            .onSuccess {
                _state.value = _state.value.copy(isAlarming = false)
            }
            .onFailure {
                _state.value = _state.value.copy(error = it.message?: "Unknown error")
                viewModelScope.launch {
                    _effect.send(NotiSirenEffect.ShowMessage("Failed to stop alarm"))
                }
            }
    }
}