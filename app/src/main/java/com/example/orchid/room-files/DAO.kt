package com.example.orchid.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface PlantDao {
    @Query("SELECT * FROM Plant")
    fun getAll(): Flow<List<Plant>>
    @Query("SELECT * FROM Plant WHERE plantID=:plantID")
    fun getByID(plantID: Int?): Plant
    @Insert
    fun insertAll(vararg category: Plant) : List<Long>
    @Update
    fun updatePlant(plant: Plant)
}

@Dao
interface WateringDao {
    @Query("SELECT * FROM Watering")
    fun getAll(): List<Watering>
}

@Dao
interface PlantPhotoDao {
    @Insert
    fun insertAllPhoto(vararg category: PlantPhoto)
}