package com.example.notisiren.ui

import com.example.notisiren.domain.AlarmStatusRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeAlarmStatusRepository: AlarmStatusRepository {
    private val _isAlarming = MutableStateFlow(false)
    override val isAlarming = _isAlarming.asStateFlow()

    override fun setAlarming(value: Boolean) { _isAlarming.value = value}
}