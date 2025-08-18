package com.example.notisiren.ui

import android.Manifest
import android.annotation.SuppressLint
import androidx.annotation.RequiresPermission
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notisiren.domain.AlarmController
import com.example.notisiren.domain.AlarmStatusRepository
import com.example.notisiren.domain.NotificationAccessChecker
import com.example.notisiren.domain.NotificationHelper
import com.example.notisiren.domain.NotificationListenerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
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
    alarmRepository: AlarmStatusRepository,
    private val notificationHelper: NotificationHelper,
    notificationListenerRepo: NotificationListenerRepository
) : ViewModel() {

    private val _local = MutableStateFlow(NotiSirenState())

    val state: StateFlow<NotiSirenState> =
        combine(
            alarmRepository.isAlarming,
            notificationListenerRepo.isListening,
            notificationAccessChecker.isEnabled(),
            _local
        ) { isAlarming, isListening, notificationAccessEnabled, local ->
            println("DEBUG: isAlarming=$isAlarming, isListening=$isListening, notificationAccessEnabled=$notificationAccessEnabled")
            local.copy(isAlarming = isAlarming, isListening = isListening, notificationAccessEnabled = notificationAccessEnabled)
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            NotiSirenState()
        )

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
            NotiSirenEvent.ClickStartAlarm -> startAlarm()
        }
    }

    private fun refreshAccess() {
        viewModelScope.launch {
            _local.update { it.copy(isLoading = true) }

            val accessFlow: Flow<Boolean> = notificationAccessChecker.isEnabled()
            val currentEnabledStatus: Boolean = withContext(Dispatchers.IO) {
                accessFlow.first()
            }

            _local.update { it.copy(notificationAccessEnabled = currentEnabledStatus, isLoading = false) }
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

    @SuppressLint("MissingPermission")
    private fun startAlarm() {
        viewModelScope.launch {
            try {
                alarmController.startAlarm()
                triggerNotification("my título", "my message")
            } catch (e: Exception) {
                _effect.send(NotiSirenEffect.ShowMessage("Failed to start alarm"))
                _local.update { it.copy(error = e.message ?: "Unknown error") }
            }
        }
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun triggerNotification(title: String, message: String) {
        notificationHelper.showAlarmNotification(title, message)
    }
}