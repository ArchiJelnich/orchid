package com.example.orchid.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.orchid.PlantMarkedViewModel
import com.example.orchid.R
import com.example.orchid.infra.localDateToString
import com.example.orchid.infra.stringToNiceString
import com.example.orchid.infra.wateringDaysAfter
import com.example.orchid.infra.wateringDaysOfMonth
import com.example.orchid.infra.wateringDaysWeek
import com.example.orchid.room.AppDatabase
import com.example.orchid.room.Plant
import com.example.orchid.room.Watering
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TodayScreen (viewModel: PlantMarkedViewModel) {

    MaterialTheme(
    ){
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val plants by viewModel.plantsMarked.collectAsState(initial = emptyList())


            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = stringResource(R.string.header_today),
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier
                        .align(alignment = Alignment.CenterHorizontally)
                        .padding(top = 40.dp)
                        .padding(bottom = 40.dp),
                    textAlign = TextAlign.Center
                )

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
                ) {
                    items(plants) { plant ->
                        PlantItem(plant)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }



                BottomPanel()

            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PlantItem(plant : Plant) {
    val context = LocalContext.current
    val date = plant.lastWateringDate
    var isToday = false


    if (date == localDateToString(LocalDate.now()))
    {
        isToday = true
    }

    var imageUri by remember { mutableStateOf<String?>(null) }

    val db: AppDatabase = AppDatabase.getInstance(context)
    val plantPhotoDao = db.PlantPhotoDao()

    LaunchedEffect(plant.plantID) {
        withContext(Dispatchers.IO) {
            val uri = plantPhotoDao.getByID(plant.plantID)
            if (uri != null) {
                withContext(Dispatchers.Main) {
                    imageUri = uri.toString()
                }
            }
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (isToday) colorResource(id = R.color.app_light_green) else colorResource(id = R.color.app_orange))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        AsyncImage(
            model = imageUri,
            contentDescription = "Flower",
            placeholder = painterResource(R.drawable.icon_flower),
            error = painterResource(R.drawable.icon_flower),
            modifier = Modifier.size(40.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = plant.plantName,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = stringToNiceString(date, context),
                fontSize = 14.sp,
                color = Color.DarkGray
            )
        }
        Row {
            IconButton(onClick = {

                Log.d("getAll", "Click")
                val db: AppDatabase = AppDatabase.getInstance(context)
                val plantDao = db.PlantDao()
                val wateringDao = db.WateringDao()
                val plantToUpdate = plant
                plantToUpdate.marked = 0

                when (plant.plantType)
                {
                    0 -> plantToUpdate.lastWateringDate = wateringDaysAfter(plant.plantSubType.toString(), LocalDate.now().plusDays(1))
                    1 -> plantToUpdate.lastWateringDate = wateringDaysWeek(plant.plantSubType.toString(), LocalDate.now().plusDays(1))
                    2 -> plantToUpdate.lastWateringDate = wateringDaysOfMonth(plant.plantSubType.toString(), LocalDate.now().plusDays(1))
                }

                val stringDate: List<String> = localDateToString(LocalDate.now()).split(".")

                val wateringToAdd = Watering(
                    wID = 0,
                    wateringPlantID = plantToUpdate.plantID,
                    wateringDate = localDateToString(LocalDate.now()),
                    wateringDay = stringDate[0],
                    wateringMonth = stringDate[1],
                    wateringYear = stringDate[2]
                )

                GlobalScope.launch {
                    plantDao.updatePlant(plantToUpdate)
                    wateringDao.insertAll(wateringToAdd)
                }




            }) {
                Icon(Icons.Default.Add, contentDescription = "+")
            }
        }

    }
}

