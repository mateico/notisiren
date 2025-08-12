package com.example.notisiren.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun NotificationAlarmScreen(
    state: NotiSirenState,
    onEvent: (NotiSirenEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        val accessLabel =
            if (state.notificationAccessEnabled) "Notificaciones habilitadas ✅"
            else "Habilitar Acceso a Notificaciones"


        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp),
        ) {
            ColoredDot(color = if (state.isListening) Color.Green else Color.Red)
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { onEvent(NotiSirenEvent.ClickEnableNotification) },
                enabled = !state.notificationAccessEnabled,
                modifier = Modifier.testTag("btn_enable_notifications")
            ) {
                Text(accessLabel)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { onEvent(NotiSirenEvent.ClickStopAlarm) },
                enabled = state.isAlarming,
            ) {
                Text(if (state.isAlarming) "Stop Alarm" else "Alarma detenida")
            }
        }
    }
}

@Composable
fun ColoredDot(
    color: Color,
    diameterDp: Dp = 20.dp
) {
    Box(
        modifier = Modifier
            .size(diameterDp)
            .background(color = color, shape = CircleShape)
    )
}

@Preview(showBackground = true)
@Composable
fun NotificationAlarmScreenPreview() {
    NotificationAlarmScreen(
        state = NotiSirenState(isListening = true),
        onEvent = {}
    )
}