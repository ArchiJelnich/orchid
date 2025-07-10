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
        var currentFlag = flagGet(this)

        Log.d("MyDebug", "flag = " + currentFlag)

        var editedPlant = Plant(
            plantID = 0,
            plantName = "",
            plantType = 0,
            plantSubType = "",
            lastWateringID = 0
        )


        if (currentFlag==102) {
            val preferences = PreferenceManager.getDefaultSharedPreferences(this)
            var editedPlantID = preferences.getInt("edit_plant_id", 0)
            GlobalScope.launch {
                editedPlant = plantDao.getByID(editedPlantID)
            }
        }


        enableEdgeToEdge()
        setContent {
            OrchidTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    Log.d("MyDebug", "editedPlant + " + editedPlant)
                    PlantEditScreen(editedPlant,currentFlag)
                }
            }
        }
    }

    override fun onPause() {
        Log.d("MyDebug", "flag onPause = " + flagGet(this))

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

            val PlantToCreate = Plant(
                plantID = 0,
                plantName = plantName.toString(),
                plantType = plantType,
                plantSubType = plantSubType.toString(),
                lastWateringID = 0
            )

            Log.d("MyDebug", "PlantToCreate onPause = " + PlantToCreate)

            GlobalScope.launch {

                val id: Long = plantDao.insertAll(PlantToCreate)[0]

                val PlantPhotoToCreate = PlantPhoto(
                    ppID = 0,
                    plantID = id.toInt(),
                    photo = plantPhoto,
                )

                Log.d("MyDebug", "PlantPhotoToCreate onPause = " + PlantPhotoToCreate)
                plantPhotoDao.insertAllPhoto(PlantPhotoToCreate)


            }




        }
        if (flagGet(this)==102)        {
            val preferences = PreferenceManager.getDefaultSharedPreferences(this)
            val plantName = preferences.getString("plantName", "")
            val plantType = preferences.getInt("plantType", 0)
            val plantSubType = preferences.getString("plantSubType", "")
            val plantID = preferences.getInt("plantID", 0)
            val plantPhoto = preferences.getString("plantPhoto", "")
            val db: AppDatabase = AppDatabase.getInstance(this)
            val plantDao = db.PlantDao()
            //val plantPhotoDao = db.PlantPhotoDao()



            val PlantToUpdate = Plant(
                plantID = plantID,
                plantName = plantName.toString(),
                plantType = plantType,
                plantSubType = plantSubType.toString(),
                lastWateringID = 0
            )



            GlobalScope.launch {

                Log.d("MyDebug", "EditPlan : onPause // PlantToUpdate = " + PlantToUpdate)
                plantDao.updatePlant(PlantToUpdate)

                //val PlantPhotoToCreate = PlantPhoto(
                //    ppID = 0,
                //    plantID = id.toInt(),
                //    photo = plantPhoto,
                //)

                //plantPhotoDao.insertAllPhoto(PlantPhotoToCreate)

            }}

        flagPut(this, 0)
        super.onPause()
    }
}