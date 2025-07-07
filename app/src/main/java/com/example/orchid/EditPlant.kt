package com.example.orchid

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
import com.example.orchid.infra.flagGet
import com.example.orchid.infra.flagPut
import com.example.orchid.room.AppDatabase
import com.example.orchid.room.Plant
import com.example.orchid.room.PlantDao
import com.example.orchid.screens.PlantEditScreen
import com.example.orchid.ui.theme.OrchidTheme
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PlantEditActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OrchidTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    PlantEditScreen()
                }
            }
        }
    }

    override fun onPause() {


        if (flagGet(this)==100)
        {
            val preferences = PreferenceManager.getDefaultSharedPreferences(this)
            val plantName = preferences.getString("plantName", "")
            val plantType = preferences.getInt("plantType", 0)
            val plantSubType = preferences.getString("plantSubType", "")
            val db: AppDatabase = AppDatabase.getInstance(this)
            val plantDao = db.PlantDao()

            val PlantToCreate = Plant(
                plantID = 0,
                plantName = plantName.toString(),
                plantType = plantType,
                plantSubType = plantSubType.toString(),
                lastWateringID = 0
            )

            GlobalScope.launch {
                Log.d("MyDebug", "To create $PlantToCreate")
                plantDao.insertAll(PlantToCreate)
            }

        }
        flagPut(this, 0)
        super.onPause()
    }
}