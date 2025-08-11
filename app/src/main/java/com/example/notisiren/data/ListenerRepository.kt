package com.example.notisiren.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object ListenerRepository {

    private val _isListening = MutableStateFlow(false)
    val isListening: StateFlow<Boolean> = _isListening

    fun setListening(value: Boolean) {
        _isListening.value = value
    }
}