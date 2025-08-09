package com.example.notisiren

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.notisiren.core.AlarmControllerImpl
import com.example.notisiren.core.NotificationAccessCheckerImpl
import com.example.notisiren.core.NotiSirenViewModel
import com.example.notisiren.ui.NotiSirenEffect

class MainActivity : ComponentActivity() {

    // Simple factory so we avoid DI for now
    private val vmFactory by lazy {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val alarm = AlarmControllerImpl(applicationContext)
                val access = NotificationAccessCheckerImpl(applicationContext)
                return NotiSirenViewModel(alarm, access) as T
            }
        }
    }

    private val viewModel: NotiSirenViewModel by viewModels { vmFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val state by viewModel.state.collectAsState()
            val snackbarHostState = remember { SnackbarHostState() }

            LaunchedEffect(Unit) {
                viewModel.effect.collect { effect ->
                    when (effect) {
                        is NotiSirenEffect.OpenNotificationAccessSettings -> {
                            startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
                        }

                        is NotiSirenEffect.ShowMessage -> {
                            snackbarHostState.showSnackbar(effect.text)
                        }
                    }
                }
            }
            Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { innerPadding ->
                NotificationAlarmScreen(
                    state = state,
                    onEvent = viewModel::onEvent,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}