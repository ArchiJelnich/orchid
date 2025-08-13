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
import androidx.lifecycle.ViewModel
import com.example.orchid.infra.checkToMark
import com.example.orchid.infra.localeChecker
import com.example.orchid.room.AppDatabase
import com.example.orchid.room.Plant
import com.example.orchid.room.PlantDao
import com.example.orchid.screens.TodayScreen
import com.example.orchid.ui.theme.OrchidTheme
import kotlinx.coroutines.flow.Flow

class TodayActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {


        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val notificationHour = preferences.getInt("notification_hour", 0)
        val notificationMinute = preferences.getInt("notification_minute", 0)



        AlarmHelper.setDailyAlarm(this, notificationHour, notificationMinute)


        localeChecker(this)
        super.onCreate(savedInstanceState)
        val db: AppDatabase = AppDatabase.getInstance(this)
        val plantDao = db.PlantDao()
        val plantMarkedViewModel = PlantMarkedViewModel(plantDao)

        checkToMark(this)


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

