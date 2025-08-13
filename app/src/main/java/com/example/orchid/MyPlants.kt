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
import com.example.orchid.infra.localeChecker
import com.example.orchid.room.AppDatabase
import com.example.orchid.room.Plant
import com.example.orchid.room.PlantDao
import com.example.orchid.screens.MyPlantsScreen
import com.example.orchid.ui.theme.OrchidTheme
import kotlinx.coroutines.flow.Flow

class MyPlantsActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        localeChecker(this)
        super.onCreate(savedInstanceState)
        val db: AppDatabase = AppDatabase.getInstance(this)
        val plantDao = db.PlantDao()
        val plantViewModel = PlantViewModel(plantDao)



        enableEdgeToEdge()
        setContent {
            OrchidTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    MyPlantsScreen(plantViewModel)
                }

            }
        }





    }
}

@RequiresApi(Build.VERSION_CODES.O)
class PlantViewModel(private val dao: PlantDao) : ViewModel() {

    val plants: Flow<List<Plant>> = dao.getAllActive()


}