package com.example.orchid.screens

import android.content.Intent
import android.os.Build
import android.preference.PreferenceManager
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
import androidx.compose.ui.Modifier
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.orchid.PlantEditActivity
import com.example.orchid.PlantViewModel
import com.example.orchid.R
import com.example.orchid.infra.flagPut
import com.example.orchid.room.AppDatabase
import com.example.orchid.room.Plant
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyPlantsScreen (viewModel: PlantViewModel) {

    MaterialTheme(
    ){
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {

            val plants by viewModel.plants.collectAsState(initial = emptyList())

            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = stringResource(R.string.header_garden),
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
                        TodayPlantItem(plant = plant)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                val context = LocalContext.current

                Button(
                onClick = {
                    context.startActivity(Intent(context, PlantEditActivity::class.java))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                ) {
                Text(stringResource(R.string.common_add))
                }

                BottomPanel()

             }

        }

    }

}


@Composable
fun TodayPlantItem(plant : Plant) {

    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFDFFFD6))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "Android Icon",
            tint = Color.Black,
            modifier = Modifier.size(40.dp)
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
                text = stringResource(R.string.plant_next_watering) + " " + plant.lastWateringDate,
                fontSize = 14.sp,
                color = Color.DarkGray
            )
        }
        Row {

            DeleteIconWithDialog(plant.plantID)

            IconButton(onClick = {
                val intent = Intent(context, PlantEditActivity::class.java)
                val preferences = PreferenceManager.getDefaultSharedPreferences(context)
                val editor = preferences.edit()
                editor.apply()
                plant.plantID?.let { preferences.edit().putInt("edit_plant_id", it).apply() }
                flagPut(context, 102)
                context.startActivity(intent) }) {
                Icon(Icons.Default.Edit, contentDescription = "+")
            }
        }

    }

}

@Composable
fun DeleteIconWithDialog(passedID : Int) {
    val context = LocalContext.current

    var showDialog by remember { mutableStateOf(false) }

    IconButton(onClick = { showDialog = true }) {
        Icon(Icons.Default.Delete, contentDescription = "-")
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(stringResource(R.string.plant_delete)) },
            dismissButton = {
                TextButton(onClick = {
                    showDialog = false
                    Log.d("MyDebug", "Cancel!")

                }) {
                    Text(stringResource(R.string.common_cancel))
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    Log.d("MyDebug", "Ok!")

                    GlobalScope.launch {
                        val db: AppDatabase = AppDatabase.getInstance(context)
                        val plantDao = db.PlantDao()
                        plantDao.updateDeleteFlagById(passedID)
                    }

                }) {
                    Text(stringResource(R.string.common_ok))
                }
            },

            )
    }
}


