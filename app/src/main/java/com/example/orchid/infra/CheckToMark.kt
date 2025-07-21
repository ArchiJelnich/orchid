package com.example.orchid.infra

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.orchid.room.AppDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
fun CheckToMark(context : Context){
    val db: AppDatabase = AppDatabase.getInstance(context)
    val plantDao = db.PlantDao()
    var plantsToCheck = plantDao.getNotMarked()
    var today = LocalDate.now()
    Log.d("DateDebug", "today " + today)

    GlobalScope.launch {
        plantsToCheck.collect { plantList ->
            plantList.forEach { plant ->
                if (plant.lastWateringDate != "")
                {
                    if (today>=StringToLocalDate(plant.lastWateringDate))
                    {
                        Log.d("DateDebug", "StringToLocalDate(plant.lastWateringDate) " + StringToLocalDate(plant.lastWateringDate))
                        plantDao.updateMarkedByID(plant.plantID)
                    }
                }
            }
        }
    }

}