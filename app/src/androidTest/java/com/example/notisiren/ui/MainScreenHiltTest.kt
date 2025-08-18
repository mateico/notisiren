package com.example.notisiren.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.example.notisiren.app.MainActivity
import com.example.notisiren.di.FakeAccessChecker
import com.example.notisiren.di.FakeNotificationListenerRepository
import com.example.notisiren.domain.AlarmController
import com.example.notisiren.domain.NotificationAccessChecker
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

    @Inject lateinit var notificationAccessChecker: NotificationAccessChecker
    private lateinit var fakeAccessCheckerInstance: FakeAccessChecker

    @Inject lateinit var notificationListenerRepo: FakeNotificationListenerRepository


    @Before
    fun setup() {
        hiltRule.inject()
        fakeAccessCheckerInstance = notificationAccessChecker as FakeAccessChecker
        fakeAccessCheckerInstance.setAccessEnabled(true)
        notificationListenerRepo.setIsListening(true)
    }

    /* color dot section */
    @Test
    fun notification_listening_dot_is_green_when_access_is_listening() {
        composeRule.waitForIdle()
        val expectedColor = Color.Green
        composeRule.onNodeWithTag("dot_noti_listening_state")
            .assert(SemanticsMatcher.expectValue(DotColor, expectedColor))
    }

    @Test
    fun notification_listening_dot_is_red_when_access_is_not_listening() {
        notificationListenerRepo.setIsListening(false)
        composeRule.waitForIdle()
        val expectedColor = Color.Red
        composeRule.onNodeWithTag("dot_noti_listening_state")
            .assert(SemanticsMatcher.expectValue(DotColor, expectedColor))
    }

    /* enable notification access button section */
    @Test
    fun enable_notifications_access_button_is_disabled_when_access_is_enabled() {
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("btn_enable_notifications").assertIsNotEnabled()
    }

    @Test
    fun enable_notifications_button_is_enabled_when_access_is_disabled() {
        fakeAccessCheckerInstance.setAccessEnabled(false)
        composeRule.waitForIdle()
        composeRule.onNodeWithTag("btn_enable_notifications").assertIsEnabled()
    }
}