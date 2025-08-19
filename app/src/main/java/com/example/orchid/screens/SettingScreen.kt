package com.example.orchid

import android.icu.util.Calendar
import android.os.Build
import android.preference.PreferenceManager
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.orchid.infra.loadLanguage
import com.example.orchid.infra.saveLanguage
import com.example.orchid.infra.setAppLocale
import com.example.orchid.room.AppDatabase
import com.example.orchid.screens.BottomPanel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SettingsScreen() {

    var statsLocaleBool = true

    val preferences = PreferenceManager.getDefaultSharedPreferences(LocalContext.current)
    val settingNotificationFlag = preferences.getInt("setting_notification", 0)
    var statsThemeBool = false

    if (settingNotificationFlag==1)
    {
        statsThemeBool = true
    }


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



                        if (isNotification){
                            preferences.edit().putInt("setting_notification", 1).apply()
                        }
                        else{
                            preferences.edit().putInt("setting_notification", 0).apply()
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 20.dp)
                        .padding(start = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DeleteAllWithDialog()
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

    val context = LocalContext.current
    val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    val currentTime = Calendar.getInstance()
    val timePickerState = rememberTimePickerState(
        initialHour = preferences.getInt("notification_hour", currentTime.get(Calendar.HOUR_OF_DAY)),
        initialMinute = preferences.getInt("notification_minute", currentTime.get(Calendar.MINUTE)),
        is24Hour = true,
    )

    LaunchedEffect(timePickerState.hour, timePickerState.minute) {
        preferences.edit()
            .putInt("notification_hour", timePickerState.hour)
            .putInt("notification_minute", timePickerState.minute)
            .apply()

    }

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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DeleteAllWithDialog() {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    Button(
        onClick = {
            showDialog = true
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
    ) {
        Text(stringResource(R.string.plant_delete_all))
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(stringResource(R.string.plant_delete_all)) },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(stringResource(R.string.common_cancel))
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false

                    val db: AppDatabase = AppDatabase.getInstance(context)
                    val wateringDao = db.WateringDao()
                    val plantDao = db.PlantDao()
                    val plantPhotoDao = db.PlantPhotoDao()

                    GlobalScope.launch  {
                        wateringDao.deleteAll()
                        plantDao.deleteAll()
                        plantPhotoDao.deleteAll()
                        val dir = context.filesDir
                        dir.listFiles()?.forEach { file ->
                            if (file.isFile) {
                                file.delete()
                            }
                        }
                    }



                }) {
                    Text(stringResource(R.string.common_ok))
                }
            },

            )
    }
}