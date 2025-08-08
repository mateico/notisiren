package com.example.notisiren

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotiSirenApp()
        }
    }
}

@Composable
fun NotiSirenApp() {
    val context = LocalContext.current
    NotificationAlarmScreen(
        onEnableNotifications = {
            context.startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
        },
        onStopAlarm = {
            AlarmUtils.stopAlarm()
        }
    )
}