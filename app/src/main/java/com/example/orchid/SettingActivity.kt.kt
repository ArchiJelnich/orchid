package com.example.orchid

import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.orchid.infra.localeChecker
import com.example.orchid.ui.theme.OrchidTheme

class SettingActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        localeChecker(this)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OrchidTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    SettingsScreen()
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val NotificationHour = preferences.getInt("notification_hour", 0)
        val NotificationMinute = preferences.getInt("notification_minute", 0)



        AlarmHelper.setDailyAlarm(this, NotificationHour, NotificationMinute)
    }
}
