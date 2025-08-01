package com.example.orchid

import android.app.Application
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.orchid.room.AppDatabase
import com.example.orchid.room.PlantPhoto
import com.example.orchid.room.Watering
import com.example.orchid.screens.CalendarScreen
import com.example.orchid.ui.theme.OrchidTheme
import kotlinx.coroutines.launch
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModelProvider
import com.example.orchid.room.WateringDao
import kotlinx.coroutines.GlobalScope
import java.time.Month


class CalendarActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
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



    private val _waterings = mutableStateOf<List<Watering>>(emptyList())
    val waterings: State<List<Watering>> = _waterings



    fun loadWaterings(month: String, year: String, plantID : Long?, wateringDao : WateringDao) {
        GlobalScope.launch {

            var new_month = when (month) {
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




            Log.d("MyWat", "m " + new_month)
            Log.d("MyWat", "year " + year)

            if (plantID == null) {
                val events = wateringDao.getWateringsForMonth(new_month, year)
                _waterings.value = events
                Log.d("MyWat", "events  getWateringsForMonth" + events)
            }
            else
            {
                val events = wateringDao.getWateringsID(year, new_month, plantID.toInt())
                Log.d("MyWat", "events  getWateringsForMonthAndID" + events)
                _waterings.value = events
            }


        }
    }
}