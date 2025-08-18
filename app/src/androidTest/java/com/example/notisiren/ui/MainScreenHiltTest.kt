package com.example.notisiren.ui

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.example.notisiren.app.MainActivity
import com.example.notisiren.di.FakeAccessChecker
import com.example.notisiren.domain.AlarmController
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class MainScreenHiltTest {

    @get:Rule(order = 0) val hiltRule = HiltAndroidRule(this)
    @get:Rule(order = 1) val composeRule = createAndroidComposeRule<MainActivity>()

    @Inject lateinit var access: FakeAccessChecker

    @Before
    fun setup() {
        hiltRule.inject()
        access.setAccessEnabled(true)
    }

    @Test
    fun enable_notifications_access_button_is_disabled_when_access_is_enabled() {
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("btn_enable_notifications").assertIsNotEnabled()
    }

    @Test
    fun enable_notifications_button_is_enabled_when_access_is_disabled() {
        access.setAccessEnabled(false)
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("btn_enable_notifications").assertIsEnabled()
    }
}