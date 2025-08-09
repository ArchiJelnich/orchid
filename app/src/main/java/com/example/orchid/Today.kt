package com.example.orchid

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.orchid.AlarmReceiver
import com.example.orchid.infra.CheckToMark
import com.example.orchid.infra.localeChecker
import com.example.orchid.room.AppDatabase
import com.example.orchid.room.Plant
import com.example.orchid.room.PlantDao
import com.example.orchid.screens.TodayScreen
import com.example.orchid.ui.theme.OrchidTheme
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.util.Calendar
import java.util.concurrent.TimeUnit

class TodayActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {


        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val NotificationHour = preferences.getInt("notification_hour", 0)
        val NotificationMinute = preferences.getInt("notification_minute", 0)



        AlarmHelper.setDailyAlarm(this, NotificationHour, NotificationMinute)


        localeChecker(this)
        super.onCreate(savedInstanceState)
        val db: AppDatabase = AppDatabase.getInstance(this)
        val plantDao = db.PlantDao()
        val plantMarkedViewModel = PlantMarkedViewModel(plantDao)

        CheckToMark(this)

       /* var localDateToday = LocalDate.now()
        var dayOfWeek = localDateToday.dayOfWeek
        Log.d("MyDebug", "dayOfWeek " + dayOfWeek)
        val db: AppDatabase = AppDatabase.getInstance(this)
        val plantDao = db.PlantDao()
        plantDao.getByType(1)
        Log.d("MyDebug", "plantDao.getByType(1) " + plantDao.getByType(1))
        */

        enableEdgeToEdge()
        setContent {
            OrchidTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    TodayScreen(plantMarkedViewModel)
                }
            }
        }






    }


}

@RequiresApi(Build.VERSION_CODES.O)
class PlantMarkedViewModel(private val dao: PlantDao) : ViewModel() {

    val plantsMarked: Flow<List<Plant>> = dao.getMarked()

}

