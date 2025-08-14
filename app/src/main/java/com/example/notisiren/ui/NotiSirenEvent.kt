package com.example.notisiren.ui

sealed interface NotiSirenEvent {
    data object ClickEnableNotification : NotiSirenEvent
    data object ClickStopAlarm : NotiSirenEvent

    data object ClickStartAlarm : NotiSirenEvent
    data object RefreshAccess : NotiSirenEvent
}