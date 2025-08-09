package com.example.notisiren.domain

interface NotificationAccessChecker {
    fun isEnabled(): Boolean
}