package com.example.orchid.screens

import android.os.Build
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
import com.example.orchid.R

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PlantEditScreen () {

    MaterialTheme(
    ){
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            style = MaterialTheme.typography.headlineLarge,
                            modifier = Modifier
                                .align(alignment = Alignment.CenterHorizontally)
                                .padding(start = 40.dp),
                            textAlign = TextAlign.Center,
                            text = stringResource(R.string.plant_name),
                        )
                    }
                }

                SmartPicker()

                Button(
                    onClick = {

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
fun SmartPicker() {

    val context = LocalContext.current
    val options = context.resources.getStringArray(R.array.plant_watering_type)
    var selectedOption by remember { mutableStateOf(options[0]) }
    var text by remember { mutableStateOf("") }
    val selectedWeekDays = remember { mutableStateListOf<Int>() }
    val selectedMonthDays = remember { mutableStateListOf<Int>() }

    Column(modifier = Modifier.padding(16.dp)) {
        var expanded by remember { mutableStateOf(false) }

        Box {
            Text(
                text = selectedOption,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true }
                    .background(Color.LightGray)
                    .padding(12.dp)
            )

            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                options.forEach { label ->
                    DropdownMenuItem(
                        text = { Text(label) },
                        onClick = {
                            selectedOption = label
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (selectedOption) {
            options[0] -> {
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text(stringResource( R.string.plant_each_day)) },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            options[1] -> {
                val days = context.resources.getStringArray(R.array.week_days)
                Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    days.forEachIndexed { index, day ->
                        val selected = selectedWeekDays.contains(index)
                        Button(
                            onClick = {
                                if (selected) selectedWeekDays.remove(index)
                                else selectedWeekDays.add(index)
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

            options[2] -> {
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