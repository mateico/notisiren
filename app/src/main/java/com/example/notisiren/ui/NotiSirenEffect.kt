package com.example.notisiren.ui

sealed interface NotiSirenEffect {
    data object OpenNotificationAccessSettings : NotiSirenEffect
    data class ShowMessage(val text: String) : NotiSirenEffect
}