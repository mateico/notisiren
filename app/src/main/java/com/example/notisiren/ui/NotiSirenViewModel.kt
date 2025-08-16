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

// Mateo: Tells Hilt to generate the necessary code to provide dependencies for this ViewModel.
// Mateo: It automatically creates a Hilt ViewModel Factory to handle constructor injection for the parameters below.
@HiltViewModel
// Mateo: These dependencies are provided via Hilt DI (@Inject constructor).
// Mateo: We're injecting interfaces (not implementations) to follow clean architecture (DOMAIN LAYER abstraction).
class NotiSirenViewModel @Inject constructor(// Mateo: @Inject tells Hilt how to provide the dependencies defined in the constructor automatically.
    // Mateo: Handles starting and stopping the alarm sound
    private val alarmController: AlarmController,
    // Mateo: Checks if notification access is enabled for the app
    private val notificationAccessChecker: NotificationAccessChecker,
    // Mateo: Exposes current alarm status as a Flow and allows updating it
    alarmRepository: AlarmStatusRepository,
    private val notificationHelper: NotificationHelper,
    // Mateo: Emits changes when notification listener service is active
    notificationListenerRepo: NotificationListenerRepository
) : ViewModel() {

    // Mateo: Holds local UI state updates (like loading, access flag, errors)
// Mateo: Used only inside the ViewModel to update the state reactively
    private val _local = MutableStateFlow(NotiSirenState())

    // Mateo: Public state exposed to the UI. It combines data from 3 sources:
// - alarmRepository: gives the current alarm status as Flow
// - notificationListenerRepo: gives the listener service status as Flow
// - _local: internal state like loading flags or error messages
// Mateo: `combine` merges all values into a single `StateFlow` to be collected by the UI
    val state: StateFlow<NotiSirenState> =
        combine(
            alarmRepository.isAlarming,           // Mateo: Flow<Boolean> for alarm status
            notificationListenerRepo.isListening, // Mateo: Flow<Boolean> for listener status
            _local                                // Mateo: MutableStateFlow with flags & errors
        ) { isAlarming, isListening, local ->
            println("DEBUG: isAlarming=$isAlarming, isListening=$isListening")

            // Mateo: Create a new state with values from repos + local state
            local.copy(isAlarming = isAlarming, isListening = isListening)
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly, // Mateo: combine block immediately — even if no one (like the UI) is collecting the state yet.
            NotiSirenState()
        )

    // Mateo: _effect is a one-time event channel (like for navigation, messages, etc).
// It's not state, so we don't use StateFlow. We use Channel to send events only once.
// The BUFFERED config makes sure events aren’t lost if no one is listening right away.
    private val _effect = Channel<NotiSirenEffect>(Channel.Factory.BUFFERED)

    // Mateo: We expose the channel as a Flow so the UI can collect the events reactively.
    val effect = _effect.receiveAsFlow()

    init {
        refreshAccess()
    }

    fun onEvent(event: NotiSirenEvent) {
        when (event) {
            NotiSirenEvent.ClickEnableNotification ->
                // Mateo: viewModelScope uses the Dispatchers.Main coroutine dispatcher by default
                // starts and runs on the main (UI) thread, unless explicitly switched with withContext(...)
                viewModelScope.launch { _effect.send(NotiSirenEffect.OpenNotificationAccessSettings) }

            NotiSirenEvent.ClickStopAlarm -> stopAlarm()
            NotiSirenEvent.RefreshAccess -> refreshAccess()
            NotiSirenEvent.ClickStartAlarm -> startAlarm()
        }
    }

    private fun refreshAccess() {
        // Mateo: coroutine scope tied to the ViewModel
        viewModelScope.launch {
            // Mateo: Set loading = true before starting the check
            _local.update { it.copy(isLoading = true) }

            // Mateo: Run the access check on the IO dispatcher (background thread)
            val enabled = withContext(Dispatchers.IO) {
                notificationAccessChecker.isEnabled()
            }

            // Mateo: Once result is ready, update state with result and set loading = false
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