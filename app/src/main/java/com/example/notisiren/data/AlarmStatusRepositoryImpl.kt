package com.example.notisiren.data

import com.example.notisiren.domain.AlarmStatusRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlarmStatusRepositoryImpl @Inject constructor() : AlarmStatusRepository {

    private val _isAlarming = MutableStateFlow(false)
    override val isAlarming = _isAlarming.asStateFlow()

    override fun setAlarming(value: Boolean) {_isAlarming.value = value}
}