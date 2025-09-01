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
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.example.orchid.MyPlantsActivity
import com.example.orchid.R
import com.example.orchid.infra.flagGetExtra
import com.example.orchid.infra.flagPut
import com.example.orchid.infra.flagPutExtra
import com.example.orchid.infra.inputChecker
import com.example.orchid.infra.wateringDaysAfter
import com.example.orchid.infra.wateringDaysOfMonth
import com.example.orchid.infra.wateringDaysWeek
import com.example.orchid.room.AppDatabase
import com.example.orchid.room.Plant
import com.example.orchid.room.PlantPhoto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate


@RequiresApi(Build.VERSION_CODES.O)

@Composable
fun PlantEditScreen(
    editedPlant: Plant,
    plantImageLink: String,
    currentFlag: Int
) {
    val context = LocalContext.current
    val defUri = plantImageLink.toUri()
    var imageUri by remember   { mutableStateOf<Uri?>(defUri) }
    var croppedBitmap by remember { mutableStateOf<Bitmap?>(null) }

    Log.d("MyDebug", "defUri = " + defUri)
    Log.d("MyDebug", "imageUri = " + imageUri)

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { pickedUri ->
            GlobalScope.launch(Dispatchers.IO) {
                val bitmap = loadBitmapAndCropCenter(context, pickedUri)
                bitmap?.let {
                    val file = File(context.filesDir, "plant_${editedPlant.plantID}.jpg")
                    FileOutputStream(file).use { out ->
                        it.compress(Bitmap.CompressFormat.JPEG, 90, out)
                    }
                    withContext(Dispatchers.Main) {
                        croppedBitmap = it
                        imageUri = file.toUri()
                    }
                }
            }
        }}

    LaunchedEffect(Unit) {
        val file = File(context.filesDir, "plant_${editedPlant.plantID}.jpg")
        if (file.exists()) {
            imageUri = file.toUri()
            croppedBitmap = loadBitmapAndCropCenter(context, imageUri!!) // suspend fun
        }
    }

    val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    val editor = preferences.edit()
    editor.apply()
    preferences.edit().putInt("plantID", 0).apply()

    var defPlantName = ""
    var defPlantType = 0
    var defPlantSubtype = ""

    if (currentFlag == 102) {
        defPlantName = editedPlant.plantName
        defPlantType = editedPlant.plantType
        defPlantSubtype = editedPlant.plantSubType
        preferences.edit().putInt("plantID", editedPlant.plantID).apply()
    }

    var plantName by rememberSaveable  { mutableStateOf(defPlantName) }
    var plantType by rememberSaveable  { mutableStateOf(defPlantType) }
    var plantSubType by rememberSaveable  { mutableStateOf(defPlantSubtype) }

    Scaffold(
        bottomBar = {
            Button(
                onClick = {
                    editor.apply()
                    if (currentFlag != 102) flagPut(context, 100)

                    preferences.edit().putString("plantName", plantName).apply()
                    preferences.edit().putInt("plantType", plantType).apply()
                    preferences.edit().putString("plantSubType", plantSubType).apply()
                    preferences.edit().putString("plantPhoto", imageUri.toString()).apply()
                    preferences.edit().putInt("marked", editedPlant.marked).apply()
                    preferences.edit().putString("lastWateringDate", editedPlant.lastWateringDate).apply()

                    if (flagGetExtra(context) == 202) {
                        val db: AppDatabase = AppDatabase.getInstance(context)
                        val plantPhotoDao = db.PlantPhotoDao()
                        val plantDao = db.PlantDao()



                        GlobalScope.launch {
                            val plantToUpdate = editedPlant.copy(
                                plantName = inputChecker(context, plantName),
                                plantType = plantType,
                                plantSubType = plantSubType,
                                lastWateringDate = if (editedPlant.marked != 1) {
                                    when (plantType) {
                                        0 -> wateringDaysAfter(plantSubType, LocalDate.now())
                                        1 -> wateringDaysWeek(plantSubType, LocalDate.now())
                                        2 -> wateringDaysOfMonth(plantSubType, LocalDate.now())
                                        else -> editedPlant.lastWateringDate
                                    }
                                } else editedPlant.lastWateringDate,
                                marked = editedPlant.marked
                            )
                            plantDao.updatePlant(plantToUpdate)

                            if (imageUri != null) {
                                val existingPhoto = plantPhotoDao.getIDByID(editedPlant.plantID)
                                if (existingPhoto != null) {
                                    existingPhoto.photo = imageUri.toString()
                                    plantPhotoDao.updatePlantPhoto(existingPhoto)
                                } else {
                                    plantPhotoDao.insertAllPhoto(
                                        PlantPhoto(0, editedPlant.plantID, imageUri.toString())
                                    )
                                }
                            } else {
                                plantPhotoDao.deleteByID(editedPlant.plantID)
                            }
                        }

                        flagPutExtra(context, 0)
                    }

                    val intent = Intent(context, MyPlantsActivity::class.java)
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 56.dp, top = 16.dp)
            ) {
                Text(stringResource(R.string.common_add))
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {



            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray)
                    .align(Alignment.CenterHorizontally)
                    .combinedClickable(
                        onClick = {
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
                        },
                        onLongClick = {
                            val file = File(context.filesDir, "plant_${editedPlant.plantID}.jpg")
                            if (file.exists()) file.delete()
                            imageUri = null
                            croppedBitmap = null
                            flagPutExtra(context, 202)
                        }
                    )
            ) {
                if (croppedBitmap != null) {
                    Image(
                        bitmap = croppedBitmap!!.asImageBitmap(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.icon_flower),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }


            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = plantName,
                onValueChange = { plantName = it },
                textStyle = MaterialTheme.typography.headlineLarge,
                placeholder = {
                    Text(
                        text = stringResource(R.string.plant_name),
                        style = MaterialTheme.typography.headlineLarge,
                        textAlign = TextAlign.Center
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
            ) {
                SmartPicker(
                    plantType = plantType,
                    onPlantTypeSelected = { plantType = it },
                    plantSubType = plantSubType,
                    onPlantSubTypeSelected = { plantSubType = it }
                )

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
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

    val options = context.resources.getStringArray(R.array.plant_watering_type)
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
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

            }

            1 -> {
                val days = context.resources.getStringArray(R.array.week_days)
                Column(modifier = Modifier
                    .fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    days.forEachIndexed { index, day ->
                        val selected = selectedWeekDays.contains(index)
                        Button(
                            onClick = {
                                if (selected) selectedWeekDays.remove(index)
                                else selectedWeekDays.add(index)
                                onPlantSubTypeSelected(selectedWeekDays.joinToString(","))
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selected) colorResource(id = R.color.app_green) else colorResource(id = R.color.app_grey)
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


                            Box(
                                modifier = Modifier
                                    .size(circleSize)
                                    .clip(CircleShape)
                                    .background(if (selected) colorResource(id = R.color.app_green) else colorResource(id = R.color.app_light_grey))
                                    .clickable {
                                        if (selected) selectedMonthDays.remove(day)
                                        else selectedMonthDays.add(day)
                                        onPlantSubTypeSelected(selectedMonthDays.joinToString(","))
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