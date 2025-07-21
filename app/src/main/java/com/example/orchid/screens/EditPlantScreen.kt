package com.example.orchid.screens

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.example.orchid.MyPlantsActivity
import com.example.orchid.R
import com.example.orchid.infra.flagGet
import com.example.orchid.infra.flagGetExtra
import com.example.orchid.infra.flagPut
import com.example.orchid.infra.flagPutExtra
import com.example.orchid.room.AppDatabase
import com.example.orchid.room.Plant
import com.example.orchid.room.PlantPhoto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URI


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PlantEditScreen (editedPlant : Plant, plantImageLink: String, currentFlag : Int) {




    MaterialTheme(
    ){
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {

            Log.d("MyDebugPhoto", "plantImageLink + " + plantImageLink)


            val defUri = plantImageLink.toUri()



            val context = LocalContext.current



            var imageUri by remember { mutableStateOf<Uri?>(defUri) }
            var croppedBitmap by remember { mutableStateOf<Bitmap?>(null) }

            Log.d("MyDebugPhotoChange", "imageUri + " + imageUri)

            val imagePickerLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetContent()
            ) { uri: Uri? ->
                imageUri = uri
            }

            LaunchedEffect(imageUri) {
                imageUri?.let { uri ->
                    val bitmap = loadBitmapAndCropCenter(context, uri)
                    croppedBitmap = bitmap
                }
            }

            Column(modifier = Modifier.fillMaxSize()) {

                val imageModifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray)
                    .clickable {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ||
                            ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            imagePickerLauncher.launch("image/*")
                            flagPutExtra(context, 202)

                        } else {
                            val activity = context as Activity
                            ActivityCompat.requestPermissions(
                                activity,
                                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                                200
                            )
                            flagPutExtra(context, 202)
                        }


                    }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (croppedBitmap != null) {
                        Image(
                            bitmap = croppedBitmap!!.asImageBitmap(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = imageModifier
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.icon_image),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = imageModifier
                        )
                    }
                }

                var def_plantName=""
                var def_plantType=0
                var def_plantSubType=""

                val preferences = PreferenceManager.getDefaultSharedPreferences(context)
                val editor = preferences.edit()
                editor.apply()
                preferences.edit().putInt("plantID", 0).apply()

                if (currentFlag==102)
                {
                    def_plantName=editedPlant.plantName
                    def_plantType=editedPlant.plantType
                    def_plantSubType=editedPlant.plantSubType
                    preferences.edit().putInt("plantID", editedPlant.plantID).apply()

                    //editedPlant.plantID?.let { preferences.edit().putInt("plantID", it).apply() }
                }



                var plantName by remember { mutableStateOf(def_plantName) }
                var plantType by remember { mutableStateOf(def_plantType) }
                var plantSubType by remember { mutableStateOf(def_plantSubType) }


                //Log.d("MyDebug", "plantType + " + plantType)
                //Log.d("MyDebug", "plantSubType + " + plantSubType)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        TextField(
                            value = plantName,
                            onValueChange = { plantName = it },
                            textStyle = MaterialTheme.typography.headlineLarge,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(start = 40.dp),
                            singleLine = true,
                            placeholder = {
                                Text(
                                    text = stringResource(R.string.plant_name),
                                    style = MaterialTheme.typography.headlineLarge,
                                    textAlign = TextAlign.Center
                                )
                            }
                        )
                    }
                }



                SmartPicker(plantType = plantType,
                    onPlantTypeSelected = { plantType = it },
                    plantSubType = plantSubType,
                    onPlantSubTypeSelected = { plantSubType = it })

                Button(
                    onClick = {

                        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
                        val editor = preferences.edit()
                        editor.apply()
                        if (currentFlag!=102)
                        {
                            flagPut(context, 100)
                        }


                        //preferences.edit().putInt("plantID", plantID).apply()
                        preferences.edit().putString("plantName", plantName).apply()
                        preferences.edit().putInt("plantType", plantType).apply()
                        preferences.edit().putString("plantSubType", plantSubType).apply()
                        preferences.edit().putString("plantPhoto", imageUri.toString()).apply()

                        Log.d ("MyDebugPhotoChange", "flagGet(context) " + flagGet(context))
                        Log.d ("MyDebugPhotoChange", "flagGetExtra(context) " + flagGetExtra(context))

                        if (flagGetExtra(context)==202)
                        {
                            val db: AppDatabase = AppDatabase.getInstance(context)
                            val plantPhotoDao = db.PlantPhotoDao()



                            GlobalScope.launch {

                                var PlantPhotoToUpdate = plantPhotoDao.getIDByID(editedPlant.plantID)
                                PlantPhotoToUpdate.photo = imageUri.toString()
                                plantPhotoDao.updatePlantPhoto(PlantPhotoToUpdate)
                                Log.d("MyDebugPhotoChange", "EditPlanScreen // PlantPhotoToUpdate = " + PlantPhotoToUpdate)

                            }

                            flagPutExtra(context, 0)
                        }

                        val intent = Intent(context, MyPlantsActivity::class.java)
                        context.startActivity(intent)

                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(stringResource(R.string.common_add))
                }


            }
            }}

}

suspend fun loadBitmapAndCropCenter(context: Context, uri: Uri): Bitmap? = withContext(Dispatchers.IO) {
    try {
        val original = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        val dimension = minOf(original.width, original.height)
        val xOffset = (original.width - dimension) / 2
        val yOffset = (original.height - dimension) / 2
        Bitmap.createBitmap(original, xOffset, yOffset, dimension, dimension)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

@Composable
fun SmartPicker(plantType: Int,
                onPlantTypeSelected: (Int) -> Unit,
                plantSubType : String,
                onPlantSubTypeSelected: (String) -> Unit) {

    val context = LocalContext.current
    var selectedOptionIndex by remember { mutableStateOf(plantType) }

    val initialWeekDays = remember {
        if (plantType == 1 && plantSubType.isNotBlank()) {
            plantSubType.split(",").mapNotNull { it.toIntOrNull() }.toMutableStateList()
        } else mutableStateListOf()
    }
    val initialMonthDays = remember {
        if (plantType == 2 && plantSubType.isNotBlank()) {
            plantSubType.split(",").mapNotNull { it.toIntOrNull() }.toMutableStateList()
        } else mutableStateListOf()
    }

    val selectedWeekDays = remember { initialWeekDays }
    val selectedMonthDays = remember { initialMonthDays }

    var selectedAdditionalInfo by remember { mutableStateOf(plantSubType) }
    val options = context.resources.getStringArray(R.array.plant_watering_type)
    val selectedOption by remember { mutableStateOf(options[0]) }
    var text by remember { mutableStateOf("") }


    Column(modifier = Modifier.padding(16.dp)) {
        var expanded by remember { mutableStateOf(false) }

        Box {
            Text(
                text = options[selectedOptionIndex],
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true }
                    .background(Color.LightGray)
                    .padding(12.dp)
            )

            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                options.forEachIndexed { index, label ->
                    DropdownMenuItem(
                        text = { Text(label) },
                        onClick = {
                            selectedOptionIndex = index
                            onPlantTypeSelected(index)
                            onPlantSubTypeSelected("")
                            text = ""
                            selectedWeekDays.clear()
                            selectedMonthDays.clear()
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (selectedOptionIndex) {
            0 -> {
                TextField(
                    value = plantSubType,
                    onValueChange = { text = it
                        onPlantSubTypeSelected(it)},
                    label = { Text(stringResource(R.string.plant_each_day)) },
                    modifier = Modifier.fillMaxWidth()
                )

            }

            1 -> {
                val days = context.resources.getStringArray(R.array.week_days)
                Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    days.forEachIndexed { index, day ->
                        val selected = selectedWeekDays.contains(index)
                        Button(
                            onClick = {
                                if (selected) selectedWeekDays.remove(index)
                                else selectedWeekDays.add(index)
                                onPlantSubTypeSelected(selectedWeekDays.joinToString(","))
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selected) Color.Green else Color.LightGray
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(day, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                        }
                    }
                }
            }

            2 -> {
                val circleSize = 40.dp
                val rows = (1..31).chunked(7)
                rows.forEach { week ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        week.forEach { day ->
                            val selected = selectedMonthDays.contains(day)
                            onPlantSubTypeSelected(selectedMonthDays.joinToString(","))

                            Box(
                                modifier = Modifier
                                    .size(circleSize)
                                    .clip(CircleShape)
                                    .background(if (selected) Color.Green else Color.LightGray)
                                    .clickable {
                                        if (selected) selectedMonthDays.remove(day)
                                        else selectedMonthDays.add(day)
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = day.toString(),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Black
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}