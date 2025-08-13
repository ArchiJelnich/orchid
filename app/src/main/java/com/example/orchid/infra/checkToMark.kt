package com.example.orchid.infra

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.orchid.room.AppDatabase
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(DelicateCoroutinesApi::class)
@RequiresApi(Build.VERSION_CODES.O)
fun checkToMark(context : Context){
    val db: AppDatabase = AppDatabase.getInstance(context)
    val plantDao = db.PlantDao()
    val plantsToCheck = plantDao.getNotMarked()
    val today = LocalDate.now()

    GlobalScope.launch {
        plantsToCheck.collect { plantList ->
            plantList.forEach { plant ->
                if (plant.lastWateringDate != "")
                {
                    if (today>=stringToLocalDate(plant.lastWateringDate))
                    {
                        plantDao.updateMarkedByID(plant.plantID)
                    }
                }
            }
        }
    }

}