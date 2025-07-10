package com.example.orchid.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "Plant")
data class Plant(
    @PrimaryKey(autoGenerate = true) var plantID: Int,
    @ColumnInfo(name = "plantName") val plantName: String,
    @ColumnInfo(name = "plantType") val plantType: Int,
    @ColumnInfo(name = "plantSubType") val plantSubType: String,
    @ColumnInfo(name = "lastWateringID") val lastWateringID: Int,
) : Serializable

@Entity(tableName = "Watering")
data class Watering(
    @PrimaryKey(autoGenerate = true) var wID: Int,
    @ColumnInfo(name = "wateringPlantID") val wateringPlantID: Int,
    @ColumnInfo(name = "wateringDate") val wateringDate: String?,
)


@Entity(tableName = "PlantPhoto")
data class PlantPhoto(
    @PrimaryKey(autoGenerate = true) var ppID: Int,
    @ColumnInfo(name = "plantID", defaultValue = "0") val plantID: Int?,
    @ColumnInfo(name = "photo", defaultValue = "0") val photo: String?,
)
