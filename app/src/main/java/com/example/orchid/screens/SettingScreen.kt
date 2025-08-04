package com.example.orchid

import android.icu.util.Calendar
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.orchid.infra.loadLanguage
import com.example.orchid.infra.saveLanguage
import com.example.orchid.infra.setAppLocale
import com.example.orchid.screens.BottomPanel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SettingsScreen() {

    var statsLocaleBool = true
    var statsThemeBool = true
    var isNotification by remember { mutableStateOf(statsThemeBool) }
    val context = LocalContext.current
    val startLocale = loadLanguage(context)
    if (startLocale != "RU")
    {
        statsLocaleBool = false
    }


    MaterialTheme(
    ){
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ){
            Box(modifier = Modifier.fillMaxSize()){
            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = stringResource(R.string.header_settings),
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier
                        .align(alignment = Alignment.CenterHorizontally)
                        .padding(top = 40.dp)
                        .padding(bottom = 40.dp),
                    textAlign = TextAlign.Center
                )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 20.dp)
                    .padding(start = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(R.string.settings_lang))
                Spacer(modifier = Modifier.weight(1f))
                var isRussian by remember { mutableStateOf(statsLocaleBool) }
                Switch(
                    checked = isRussian,
                    onCheckedChange = {
                        isRussian = it
                        var locale = "ENG"
                        if (isRussian) {
                            locale = "RU"
                        }

                        setAppLocale(context, locale)
                        saveLanguage(context, locale)

                    }
                )
                Text(text = if (isRussian)
                {
                    stringResource(R.string.settings_ru)
                }
                else {
                    stringResource(R.string.settings_eng)
                },
                    modifier = Modifier.padding(start = 8.dp))
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 20.dp)
                    .padding(start = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(R.string.settings_notification))
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = isNotification,
                    onCheckedChange = {
                        isNotification = it
                        var notification = "on"

                        if (isNotification){
                            notification = "off"
                        }


                    }
                )
                Text(text = if (isNotification) stringResource(R.string.settings_on) else stringResource(R.string.settings_off), modifier = Modifier.padding(start = 8.dp))
            }

             if (isNotification){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 20.dp)
                        .padding(start = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    ShowPicker()
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowPicker(){

    val currentTime = Calendar.getInstance()
    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true,
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        TimeInput(
            state = timePickerState
        )
    }

}