package com.example.orchid

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.lifecycle.ViewModel
import com.example.orchid.room.AppDatabase
import com.example.orchid.room.Plant
import com.example.orchid.room.PlantDao
import com.example.orchid.screens.GardenScreen
import com.example.orchid.screens.TodayScreen
import com.example.orchid.ui.theme.OrchidTheme
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TodayActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db: AppDatabase = AppDatabase.getInstance(this)
        val plantDao = db.PlantDao()
        val plantViewModel = PlantViewModel(plantDao)

      /*  GlobalScope.launch  {
            val plant = Plant(
                plantID = 0,
                plantName = "Rose",
                plantType = 0,
                plantSubType = 0,
                lastWateringID = 0,
            )
            val plant2 = Plant(
                plantID = 0,
                plantName = "RoseWhite",
                plantType = 0,
                plantSubType = 0,
                lastWateringID = 0,
            )

            plantDao.insertAll(plant)
            plantDao.insertAll(plant2)
        }

*/






        enableEdgeToEdge()
        setContent {
            OrchidTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    TodayScreen(plantViewModel)
                }

            }
        }





    }
}

@RequiresApi(Build.VERSION_CODES.O)
class PlantViewModel(private val dao: PlantDao) : ViewModel() {

    val plants: Flow<List<Plant>> = dao.getAll()


}