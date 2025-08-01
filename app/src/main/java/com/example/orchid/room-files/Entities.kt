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
    // 0 - every X days
    // 1 - day of week
    // 2 - day of mounth
    @ColumnInfo(name = "plantSubType") val plantSubType: String,
    @ColumnInfo(name = "lastWateringDate") var lastWateringDate: String,
    @ColumnInfo(name = "marked") var marked: Int,
    // 0 - not
    // 1 - marked
    @ColumnInfo(name = "deleteFlag", defaultValue = "0") val deleteFlag: Int = 0,
) : Serializable

@Entity(tableName = "Watering")
data class Watering(
    @PrimaryKey(autoGenerate = true) var wID: Int,
    @ColumnInfo(name = "wateringPlantID") val wateringPlantID: Int,
    @ColumnInfo(name = "wateringDate") val wateringDate: String?,
    @ColumnInfo(name = "wateringDay") val wateringDay: String,
    @ColumnInfo(name = "wateringMonth") val wateringMonth: String?,
    @ColumnInfo(name = "wateringYear") val wateringYear: String?,
)


@Entity(tableName = "PlantPhoto")
data class PlantPhoto(
    @PrimaryKey(autoGenerate = true) var ppID: Int,
    @ColumnInfo(name = "plantID", defaultValue = "0") val plantID: Int?,
    @ColumnInfo(name = "photo", defaultValue = "") var photo: String = "",
)
