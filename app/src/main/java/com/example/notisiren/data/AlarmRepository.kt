package com.example.notisiren.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object AlarmRepository {

    private val _isAlarming = MutableStateFlow(false)
    val isAlarming: StateFlow<Boolean> = _isAlarming.asStateFlow()

    fun setAlarming(value: Boolean) {_isAlarming.value = value}
}