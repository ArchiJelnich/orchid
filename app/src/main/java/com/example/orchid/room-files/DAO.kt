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
    @Query("SELECT * FROM Plant WHERE deleteFlag=0")
    fun getAllActive(): Flow<List<Plant>>
    @Query("SELECT * FROM Plant WHERE plantID=:plantID")
    fun getByID(plantID: Int?): Plant
    @Insert
    fun insertAll(vararg category: Plant) : List<Long>
    @Update
    fun updatePlant(plant: Plant)
    @Query("UPDATE Plant SET deleteFlag = 1 WHERE plantID = :plantId")
    fun updateDeleteFlagById(plantId: Int)
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
    @Query("SELECT photo FROM PlantPhoto WHERE plantID=:plantID")
    fun getByID(plantID: Int?): String
    @Query("SELECT * FROM PlantPhoto WHERE plantID=:plantID")
    fun getIDByID(plantID: Int?): PlantPhoto
    @Update
    fun updatePlantPhoto(plantPhoto: PlantPhoto)
    @Query("SELECT * FROM PlantPhoto")
    fun getAll(): List<PlantPhoto>
}