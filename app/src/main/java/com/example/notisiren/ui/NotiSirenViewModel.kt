package com.example.notisiren.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notisiren.domain.AlarmController
import com.example.notisiren.domain.AlarmStatusRepository
import com.example.notisiren.domain.NotificationAccessChecker
import com.example.notisiren.domain.NotificationListenerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class NotiSirenViewModel @Inject constructor(
    private val alarm: AlarmController,
    private val access: NotificationAccessChecker,
    private val alarmStatusRepo: AlarmStatusRepository,
    private val notificationListenerRepo: NotificationListenerRepository
) : ViewModel() {

    private val _state = MutableStateFlow(NotiSirenState())
    val state: StateFlow<NotiSirenState> = _state.asStateFlow()

    private val _effect = Channel<NotiSirenEffect>(Channel.Factory.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        viewModelScope.launch {
            alarmStatusRepo.isAlarming.collectLatest { running ->
                _state.value = _state.value.copy(isAlarming = running)
            }
        }
        viewModelScope.launch {
            notificationListenerRepo.isListening.collectLatest { listening ->
                _state.value = _state.value.copy(isListening = listening)
            }
        }
        refreshAccess()
    }

    fun onEvent(event: NotiSirenEvent) {
        when (event) {
            NotiSirenEvent.ClickEnableNotification ->
                viewModelScope.launch { _effect.send(NotiSirenEffect.OpenNotificationAccessSettings) }

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
        runCatching {
            alarm.stopAlarm()
            alarmStatusRepo.setAlarming(false)
        }
            .onSuccess {
                _state.value = _state.value.copy(isAlarming = false)
            }
            .onFailure {
                _state.value = _state.value.copy(error = it.message ?: "Unknown error")
                viewModelScope.launch {
                    _effect.send(NotiSirenEffect.ShowMessage("Failed to stop alarm"))
                }

            }
    }
}