package com.example.orchid

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.orchid.room.AppDatabase
import com.example.orchid.screens.TodayScreen
import com.example.orchid.ui.theme.OrchidTheme
import java.time.LocalDate

class TodayActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                    TodayScreen()
                }
            }
        }
    }
}