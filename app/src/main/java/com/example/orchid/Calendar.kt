package com.example.orchid

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import com.example.orchid.room.Watering
import com.example.orchid.screens.CalendarScreen
import com.example.orchid.ui.theme.OrchidTheme
import kotlinx.coroutines.launch
import androidx.compose.runtime.State
import com.example.orchid.infra.localeChecker
import com.example.orchid.room.WateringDao
import kotlinx.coroutines.GlobalScope


class CalendarActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        localeChecker(this)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val calendarViewModel = CalendarViewModel()
        setContent {
            OrchidTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    CalendarScreen(viewModel = calendarViewModel)
                }
            }
        }
    }
}


class CalendarViewModel() {

    private val _watering = mutableStateOf<List<Watering>>(emptyList())
    val watering: State<List<Watering>> = _watering



    fun loadWatering(month: String, year: String, plantID : Long?, wateringDao : WateringDao) {
        GlobalScope.launch {

            val newMonth = when (month) {
                "JANUARY" -> "1"
                "FEBRUARY" -> "2"
                "MARCH" -> "3"
                "APRIL" -> "4"
                "MAY" -> "5"
                "JUNE" -> "6"
                "JULY" -> "7"
                "AUGUST" -> "8"
                "SEPTEMBER" -> "9"
                "OCTOBER" -> "10"
                "NOVEMBER" -> "11"
                "DECEMBER" -> "12"
                else -> month
            }


            if (plantID == null) {
                val events = wateringDao.getWateringForMonth(newMonth, year)
                _watering.value = events
            }
            else
            {
                val events = wateringDao.getWateringID(year, newMonth, plantID.toInt())
                _watering.value = events
            }


        }
    }
}