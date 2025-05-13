package com.example.orchid.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface PlantDao {
    @Query("SELECT * FROM Plant")
    fun getAllAverage(): List<Plant>
}

@Dao
interface WateringDao {
    @Query("SELECT * FROM Watering")
    fun getAllAverage(): List<Watering>
}