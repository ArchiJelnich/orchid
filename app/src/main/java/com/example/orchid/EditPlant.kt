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
import com.example.orchid.infra.inputChecker
import com.example.orchid.infra.flagGet
import com.example.orchid.infra.flagPut
import com.example.orchid.infra.localeChecker
import com.example.orchid.infra.wateringDaysAfter
import com.example.orchid.infra.wateringDaysOfMonth
import com.example.orchid.infra.wateringDaysWeek
import com.example.orchid.room.AppDatabase
import com.example.orchid.room.Plant
import com.example.orchid.room.PlantPhoto
import com.example.orchid.screens.PlantEditScreen
import com.example.orchid.ui.theme.OrchidTheme
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDate

class PlantEditActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        localeChecker(this)
        super.onCreate(savedInstanceState)
        val db: AppDatabase = AppDatabase.getInstance(this)
        val plantDao = db.PlantDao()
        val plantPhotoDao = db.PlantPhotoDao()
        val currentFlag = flagGet(this)


        var editedPlant = Plant(
            plantID = 0,
            plantName = "",
            plantType = 0,
            plantSubType = "",
            lastWateringDate = "",
            marked = 0,
            deleteFlag = 0,
        )

        var plantImageLink = ""


        if (currentFlag==102 || currentFlag==202) {
            val preferences = PreferenceManager.getDefaultSharedPreferences(this)
            val editedPlantID = preferences.getInt("edit_plant_id", 0)
            GlobalScope.launch {
                editedPlant = plantDao.getByID(editedPlantID)
                plantImageLink = plantPhotoDao.getByID(editedPlantID)
                Log.d("MyDebug", "plantImageLink = " + plantImageLink)
                if (plantImageLink==null)
                {
                    plantImageLink=""
                }
                Log.d("MyDebug", "plantImageLink = " + plantImageLink)

            }
        }




        enableEdgeToEdge()
        setContent {
            OrchidTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    PlantEditScreen(editedPlant,plantImageLink, currentFlag)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onPause() {

        if (flagGet(this)==100)
        {
            val preferences = PreferenceManager.getDefaultSharedPreferences(this)
            val plantName = preferences.getString("plantName", "")
            val plantType = preferences.getInt("plantType", 0)
            val plantSubType = preferences.getString("plantSubType", "")
            val plantPhoto = preferences.getString("plantPhoto", "")
            val db: AppDatabase = AppDatabase.getInstance(this)
            val plantDao = db.PlantDao()
            val plantPhotoDao = db.PlantPhotoDao()
            var lastWateringDate = ""

            when (plantType)
            {
                0 -> lastWateringDate= wateringDaysAfter(plantSubType.toString(), LocalDate.now())
                1 -> lastWateringDate = wateringDaysWeek(plantSubType.toString(), LocalDate.now())
                2 -> lastWateringDate = wateringDaysOfMonth(plantSubType.toString(), LocalDate.now())
            }


            val PlantToCreate = Plant(
                plantID = 0,
                plantName = inputChecker(this, plantName.toString()),
                plantType = plantType,
                plantSubType = plantSubType.toString(),
                lastWateringDate = lastWateringDate,
                marked = 0,
                deleteFlag = 0,
            )


            GlobalScope.launch {

                val id: Long = plantDao.insertAll(PlantToCreate)[0]

                val plantPhotoToCreate = PlantPhoto(
                    ppID = 0,
                    plantID = id.toInt(),
                    photo = plantPhoto.toString(),
                )

                plantPhotoDao.insertAllPhoto(plantPhotoToCreate)


            }


            flagPut(this, 0)

        }
        if (flagGet(this)==102 || flagGet(this)==202)        {
            val preferences = PreferenceManager.getDefaultSharedPreferences(this)
            val plantName = preferences.getString("plantName", "")
            val plantType = preferences.getInt("plantType", 0)
            val plantSubType = preferences.getString("plantSubType", "")
            val plantID = preferences.getInt("plantID", 0)
            val marked = preferences.getInt("marked", 0)
            var lastWateringDate = preferences.getString("lastWateringDate", "")
            val db: AppDatabase = AppDatabase.getInstance(this)
            val plantDao = db.PlantDao()

            if (marked != 1)
            {
                when (plantType)
                {
                    0 -> lastWateringDate= wateringDaysAfter(plantSubType.toString(), LocalDate.now())
                    1 -> lastWateringDate = wateringDaysWeek(plantSubType.toString(), LocalDate.now())
                    2 -> lastWateringDate = wateringDaysOfMonth(plantSubType.toString(), LocalDate.now())
                }
            }

            val plantToUpdate = Plant(
                plantID = plantID,
                plantName = inputChecker(this, plantName.toString()),
                plantType = plantType,
                plantSubType = plantSubType.toString(),
                lastWateringDate = lastWateringDate.toString(),
                marked = marked,
                deleteFlag = 0,
            )


            GlobalScope.launch {
                plantDao.updatePlant(plantToUpdate)
            }


                flagPut(this, 0)

        }


        super.onPause()
    }
}