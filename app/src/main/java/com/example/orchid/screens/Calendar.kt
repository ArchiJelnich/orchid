package com.example.orchid.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.orchid.R
import com.example.orchid.room.AppDatabase
import java.time.YearMonth
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.heightIn
import com.example.orchid.CalendarViewModel
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.orchid.PlantViewModel
import com.example.orchid.room.Plant
import java.time.Month


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarScreen(viewModel: CalendarViewModel) {
    val watering by viewModel.watering
    val db: AppDatabase = AppDatabase.getInstance(LocalContext.current)
    val wateringDao = db.WateringDao()
    val plantDao = db.PlantDao()
    val plantViewModel = PlantViewModel(plantDao)
    var selectedPlant by remember { mutableStateOf<Plant?>(null) }
    val context = LocalContext.current
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }


    LaunchedEffect(currentMonth) {
        viewModel.loadWatering(
            currentMonth.monthValue.toString(),
            currentMonth.year.toString(),
            plantID = null,
            wateringDao = wateringDao
        )
    }

    val wateringMap = remember(watering) {
        watering.groupBy { it.wateringDay.toIntOrNull() ?: -1 }
    }

    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = stringResource(R.string.header_calendar),
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 40.dp, bottom = 40.dp),
                        textAlign = TextAlign.Center
                    )

                    Box(
                        modifier = Modifier
                            .padding(start = 100.dp)
                    ) {
                        сalendarFilter(
                            viewModel = plantViewModel,
                            selectedPlant = selectedPlant,
                            onPlantSelected = { plant ->
                                selectedPlant = plant
                                if (plant != null) {
                                    viewModel.loadWatering(
                                        month = currentMonth.month.toString(),
                                        year = currentMonth.year.toString(),
                                        plantID = plant.plantID.toLong(),
                                        wateringDao = wateringDao
                                    )
                                }
                            },
                            onClearSelected = {
                                selectedPlant = null
                                viewModel.loadWatering(
                                    month = currentMonth.month.toString(),
                                    year = currentMonth.year.toString(),
                                    plantID = null,
                                    wateringDao = wateringDao
                                )
                            }
                        )
                    }


                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(onClick = { currentMonth = currentMonth.minusMonths(1)
                            selectedPlant = null
                            viewModel.loadWatering(
                                currentMonth.month.minus(1).toString(),
                                currentMonth.year.toString(),
                                plantID = null,
                                wateringDao = wateringDao
                            )}) {
                            Text("<")
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        var niceMonth = when (currentMonth.month) {
                            Month.JANUARY -> context.getString(R.string.JANUARY)
                            Month.FEBRUARY -> context.getString(R.string.FEBRUARY)
                            Month.MARCH -> context.getString(R.string.MARCH)
                            Month.APRIL -> context.getString(R.string.APRIL)
                            Month.MAY -> context.getString(R.string.MAY)
                            Month.JUNE -> context.getString(R.string.JUNE)
                            Month.JULY -> context.getString(R.string.JULY)
                            Month.AUGUST -> context.getString(R.string.AUGUST)
                            Month.SEPTEMBER -> context.getString(R.string.SEPTEMBER)
                            Month.OCTOBER -> context.getString(R.string.OCTOBER)
                            Month.NOVEMBER -> context.getString(R.string.NOVEMBER)
                            Month.DECEMBER -> context.getString(R.string.DECEMBER)
                            else -> ""
                        }

                        Text(

                            text = niceMonth + " " + currentMonth.year.toString(),
                            fontSize = 20.sp
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Button(onClick = { currentMonth = currentMonth.plusMonths(1)
                            selectedPlant = null
                            viewModel.loadWatering(
                                currentMonth.month.minus(1).toString(),
                                currentMonth.year.toString(),
                                plantID = null,
                                wateringDao = wateringDao
                            )}) {
                            Text(">")
                        }
                    }

                    val weekDays = context.resources.getStringArray(R.array.week_days_short)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        weekDays.forEach {
                            Text(
                                text = it,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    val daysInMonth = currentMonth.atDay(1).lengthOfMonth()
                    val firstDayOfWeek = currentMonth.atDay(1).dayOfWeek.value % 7

                    var dayCounter = 1
                    Column(modifier = Modifier.fillMaxWidth()) {
                        for (week in 0..5) {
                            Row(modifier = Modifier.fillMaxWidth()) {
                                for (day in 0..6) {
                                    if (week == 0 && day < firstDayOfWeek) {
                                        Spacer(modifier = Modifier.weight(1f))
                                    } else if (dayCounter <= daysInMonth) {
                                        val eventsToday = wateringMap[dayCounter] ?: emptyList()


                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .padding(4.dp)
                                                .border(1.dp, Color.Black)
                                                .heightIn(min = 75.dp)
                                                .padding(8.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                Text(text = dayCounter.toString())

                                                Row(
                                                    modifier = Modifier.padding(top = 4.dp),
                                                    horizontalArrangement = Arrangement.Center
                                                ) {
                                                    eventsToday.forEach { event ->

                                                            Text("•", fontSize = 24.sp, modifier = Modifier.padding(end = 2.dp))

                                                    }
                                                }
                                            }
                                        }
                                        dayCounter++
                                    } else {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                            }
                        }
                    }
                }





                Box(
                    modifier = Modifier.align(Alignment.BottomCenter)
                ) {
                    BottomPanel()
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun сalendarFilter(
    viewModel: PlantViewModel,
    selectedPlant: Plant?,
    onPlantSelected: (Plant?) -> Unit,
    onClearSelected: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val plants by viewModel.plants.collectAsState(initial = emptyList())

    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(id = R.drawable.filter),
            contentDescription = null,
            modifier = Modifier
                .clickable { expanded = true }
                .size(24.dp)
        )

        Text(
            text = selectedPlant?.plantName ?: "",
            modifier = Modifier.padding(start = 8.dp)
        )

        if (selectedPlant != null) {
            Icon(
                painter = painterResource(R.drawable.filter_reset),
                contentDescription = "",
                modifier = Modifier
                    .padding(start = 8.dp)
                    .clickable { onClearSelected() }
            )
        }
    }

    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
        plants.forEach { plant ->
            DropdownMenuItem(
                text = { Text(plant.plantName) },
                onClick = {
                    expanded = false
                    onPlantSelected(plant)
                }
            )
        }
    }
}