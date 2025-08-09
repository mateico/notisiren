package com.example.notisiren.ui

data class NotiSirenState(
    val notificationAccessEnabled: Boolean = false,
    val isAlarming: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)