package com.example.notisiren

import android.os.Bundle
import androidx.activity.ComponentActivity
import android.content.Intent
import android.provider.Settings
import android.widget.Button

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btnEnableNotificationAccess).setOnClickListener {
            startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
        }

        findViewById<Button>(R.id.btnStartAlarm).setOnClickListener {
            AlarmUtils.startAlarm(this)
        }

        findViewById<Button>(R.id.btnStopAlarm).setOnClickListener {
            AlarmUtils.stopAlarm()
        }
    }
}