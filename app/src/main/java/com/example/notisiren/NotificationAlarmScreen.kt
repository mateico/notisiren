package com.example.notisiren

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun NotificationAlarmScreen(
    onEnableNotifications: () -> Unit,
    onStopAlarm: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = onEnableNotifications) {
            Text("Habilitar Acceso a Notificaciones")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onStopAlarm) {
            Text("Stop Alarm")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationAlarmScreenPreview() {
    NotificationAlarmScreen(
        onEnableNotifications = {},
        onStopAlarm = {}
    )
}