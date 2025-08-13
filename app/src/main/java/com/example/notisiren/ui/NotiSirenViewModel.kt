package com.example.notisiren.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notisiren.domain.AlarmController
import com.example.notisiren.domain.AlarmStatusRepository
import com.example.notisiren.domain.NotificationAccessChecker
import com.example.notisiren.domain.NotificationListenerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class NotiSirenViewModel @Inject constructor(
    private val alarmController: AlarmController,
    private val notificationAccessChecker: NotificationAccessChecker,
    private val alarmRepository: AlarmStatusRepository,
    private val notificationListenerRepo: NotificationListenerRepository
) : ViewModel() {

    private val _local = MutableStateFlow(NotiSirenState())

    // the state is derived using combine which reads from the repos.
    val state: StateFlow<NotiSirenState> =
        combine(
            alarmRepository.isAlarming,
            notificationListenerRepo.isListening,
            _local
        ) { isAlarming ,isListening, local ->
            local.copy( isAlarming = isAlarming, isListening = isListening)
        }.stateIn(viewModelScope, SharingStarted.Eagerly, NotiSirenState())

    private val _effect = Channel<NotiSirenEffect>(Channel.Factory.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
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
        viewModelScope.launch {
            _local.update { it.copy(isLoading = true) }

            val enabled = withContext(Dispatchers.IO) {
                notificationAccessChecker.isEnabled()
            }

            _local.update { it.copy(notificationAccessEnabled = enabled, isLoading = false) }
        }

    }

    private fun stopAlarm() {
        viewModelScope.launch {
            try {
                alarmController.stopAlarm()
            } catch (e: Exception) {
                _effect.send(NotiSirenEffect.ShowMessage("Failed to stop alarm"))
                _local.update { it.copy(error = e.message ?: "Unknown error") }
            }
        }
    }
}