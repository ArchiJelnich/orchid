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

class PlantEditActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db: AppDatabase = AppDatabase.getInstance(this)
        val plantDao = db.PlantDao()
        val plantPhotoDao = db.PlantPhotoDao()
        var currentFlag = flagGet(this)

        Log.d("MyDebugPhotoChange", "onCreate : OnCreate")
        Log.d("MyDebugPhotoChange", "onCreate : flag = " + currentFlag)

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
            var editedPlantID = preferences.getInt("edit_plant_id", 0)
            GlobalScope.launch {
                editedPlant = plantDao.getByID(editedPlantID)
                plantImageLink = plantPhotoDao.getByID(editedPlantID)
                Log.d("MyDebugPhotoChange", "onCreate : edit_plant_id + " + editedPlantID.toString())
                Log.d("MyDebugPhotoChange", "onCreate : plantPhotoDao.getByID(editedPlantID) + " + plantPhotoDao.getByID(editedPlantID).toString())
                Log.d("MyDebugPhotoChange", "onCreate : plantPhotoDao.getAll + " + plantPhotoDao.getAll().toString())
                Log.d("MyDebugPhotoChange", "onCreate : BREAKE POINT")
            }
        }




        enableEdgeToEdge()
        setContent {
            OrchidTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    Log.d("MyDebug", "onCreate : editedPlant + " + editedPlant)
                    Log.d("MyDebugPhotoChange", "onCreate : plantImageLink = " + plantImageLink)
                    PlantEditScreen(editedPlant,plantImageLink, currentFlag)
                }
            }
        }
    }

    override fun onResume() {
        Log.d("MyDebugPhotoChange", "OnResume")
        super.onResume()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onPause() {
        Log.d("MyDebugPhotoChange", "OnPause")
        Log.d("MyDebugPhotoChange", "flag onPause = " + flagGet(this))

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
                0 -> lastWateringDate= wateringDaysAfter(plantSubType.toString())
                1 -> lastWateringDate = wateringDaysWeek(plantSubType.toString())
                2 -> lastWateringDate = wateringDaysOfMonth(plantSubType.toString())
            }

            Log.d("lastWateringDate", "lastWateringDate " + lastWateringDate)

            Log.d("Mounth", "wateringDaysOfMonth " + wateringDaysOfMonth(plantSubType.toString()))

            val PlantToCreate = Plant(
                plantID = 0,
                plantName = plantName.toString(),
                plantType = plantType,
                plantSubType = plantSubType.toString(),
                lastWateringDate = lastWateringDate,
                marked = 0,
                deleteFlag = 0,
            )

            Log.d("lastWateringDate", "PlantToCreate onPause = " + PlantToCreate)

            GlobalScope.launch {

                val id: Long = plantDao.insertAll(PlantToCreate)[0]

                val PlantPhotoToCreate = PlantPhoto(
                    ppID = 0,
                    plantID = id.toInt(),
                    photo = plantPhoto.toString(),
                )

                Log.d("MyWeekDebug", "PlantPhotoToCreate onPause = " + PlantPhotoToCreate)
                plantPhotoDao.insertAllPhoto(PlantPhotoToCreate)


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
            val plantPhoto = preferences.getString("plantPhoto", "")
            val db: AppDatabase = AppDatabase.getInstance(this)
            val plantDao = db.PlantDao()
            val plantPhotoDao = db.PlantPhotoDao()

            Log.d("lastWateringDate", "lastWateringDate " + lastWateringDate)

            if (marked != 1)
            {
                when (plantType)
                {
                    0 -> lastWateringDate= wateringDaysAfter(plantSubType.toString())
                    1 -> lastWateringDate = wateringDaysWeek(plantSubType.toString())
                    2 -> lastWateringDate = wateringDaysOfMonth(plantSubType.toString())
                }
            }

            val PlantToUpdate = Plant(
                plantID = plantID,
                plantName = plantName.toString(),
                plantType = plantType,
                plantSubType = plantSubType.toString(),
                lastWateringDate = lastWateringDate.toString(),
                marked = marked,
                deleteFlag = 0,
            )




7
            GlobalScope.launch {

                Log.d("lastWateringDate", "EditPlan : onPause // PlantToUpdate = " + PlantToUpdate)
                plantDao.updatePlant(PlantToUpdate)

                //val PlantPhotoToCreate = PlantPhoto(
                //    ppID = 0,
                //    plantID = id.toInt(),
                //    photo = plantPhoto,
                //)

                //plantPhotoDao.insertAllPhoto(PlantPhotoToCreate)

            }


                flagPut(this, 0)

        }


        super.onPause()
    }
}