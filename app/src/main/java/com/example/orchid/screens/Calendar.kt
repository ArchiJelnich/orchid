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
import java.time.YearMonth
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarScreen() {

    MaterialTheme(
    ){
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Box(modifier = Modifier.fillMaxSize()){
            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = stringResource(R.string.header_calendar),
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier
                        .align(alignment = Alignment.CenterHorizontally)
                        .padding(top = 40.dp)
                        .padding(bottom = 40.dp),
                    textAlign = TextAlign.Center
                )
                var currentMonth by remember { mutableStateOf(YearMonth.now()) }

                Column(modifier = Modifier.fillMaxSize()) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(onClick = { currentMonth = currentMonth.minusMonths(1) }) {
                            Text("Previous")
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Text(
                            text = currentMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                            fontSize = 20.sp
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Button(onClick = { currentMonth = currentMonth.plusMonths(1) }) {
                            Text("Next")
                        }
                    }

                    val weekDays = listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс")

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        for (day in weekDays) {
                            Text(
                                text = day,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    val daysInMonth = currentMonth.atDay(1).lengthOfMonth()
                    val firstDayOfWeek =
                        currentMonth.atDay(1).dayOfWeek.ordinal // Get the first day of the month

                    Column(modifier = Modifier.fillMaxWidth()) {
                        var dayCounter = 1
                        for (week in 0..5) { // 6 rows (weeks)
                            Row(modifier = Modifier.fillMaxWidth()) {
                                for (day in 0..6) { // 7 days per row
                                    if (week == 0 && day < firstDayOfWeek) {
                                        Spacer(modifier = Modifier.weight(1f))
                                    } else if (dayCounter <= daysInMonth) {
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .padding(4.dp)
                                                .border(1.dp, Color.Black)
                                                .padding(8.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(text = dayCounter.toString())
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

            }

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                ) {
                    BottomPanel()
                }

            }
        }
    }
}
