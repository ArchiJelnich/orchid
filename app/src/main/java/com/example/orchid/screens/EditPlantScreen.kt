package com.example.orchid.screens

import android.content.Intent
import android.os.Build
import android.preference.PreferenceManager
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
import androidx.compose.ui.Modifier
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.example.orchid.R
import com.example.orchid.TodayActivity
import com.example.orchid.infra.flagPut
import com.example.orchid.room.Plant

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PlantEditScreen () {

    MaterialTheme(
    ){
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {

            val context = LocalContext.current


            Column(modifier = Modifier.fillMaxSize()) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ){
                Image(
                    painter = painterResource(id = R.drawable.icon_image),
                    contentDescription = "",
                    modifier = Modifier
                        .size(200.dp)
                )}

                var plantName by remember { mutableStateOf("") }
                var plantType by remember { mutableStateOf(0) }
                var plantSubType by remember { mutableStateOf("") }


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
                        flagPut(context, 100)
                        //preferences.edit().putInt("plantID", plantID).apply()
                        preferences.edit().putString("plantName", plantName).apply()
                        preferences.edit().putInt("plantType", plantType).apply()
                        preferences.edit().putString("plantSubType", plantSubType).apply()
                        //preferences.edit().putInt("lastWateringID", lastWateringID).apply()
                        val intent = Intent(context, TodayActivity::class.java)
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


@Composable
fun SmartPicker(plantType: Int,
                onPlantTypeSelected: (Int) -> Unit,
                plantSubType : String,
                onPlantSubTypeSelected: (String) -> Unit) {

    val context = LocalContext.current
    var selectedOptionIndex by remember { mutableStateOf(plantType) }
    var selectedAdditionalInfo by remember { mutableStateOf(plantSubType) }
    val options = context.resources.getStringArray(R.array.plant_watering_type)
    val selectedOption by remember { mutableStateOf(options[0]) }
    var text by remember { mutableStateOf("") }
    val selectedWeekDays = remember { mutableStateListOf<Int>() }
    val selectedMonthDays = remember { mutableStateListOf<Int>() }

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
                    value = text,
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