package com.example.notisiren

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.notisiren.ui.NotiSirenEvent
import com.example.notisiren.ui.NotiSirenState

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
            if(state.notificationAccessEnabled) "Notificaciones habilitadas ✅"
        else "Habilitar Acceso a Notificaciones"

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp),
        ) {
            Button(
                onClick = {onEvent(NotiSirenEvent.ClickEnableNotification)},
                enabled = !state.notificationAccessEnabled
            ) {
                Text(accessLabel)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { onEvent(NotiSirenEvent.ClickStopAlarm) },
                enabled = state.isAlarming
            ) {
                Text(if (state.isAlarming) "Stop Alarm" else "Alarma detenida")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationAlarmScreenPreview() {
    NotificationAlarmScreen(
        state = NotiSirenState(),
        onEvent = {}
    )
}