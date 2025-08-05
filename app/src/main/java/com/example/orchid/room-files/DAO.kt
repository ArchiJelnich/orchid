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
    @Query("UPDATE Plant SET marked = 1 WHERE plantID = :plantId")
    fun updateMarkedByID(plantId: Int)
    @Query("SELECT * FROM Plant WHERE plantType=:plantType")
    fun getByType(plantType: Int?): Plant
    @Query("SELECT * FROM Plant WHERE marked=1 AND deleteFlag=0")
    fun getMarked(): Flow<List<Plant>>
    @Query("SELECT * FROM Plant WHERE marked=0")
    fun getNotMarked(): Flow<List<Plant>>
    @Query("DELETE FROM Plant")
    fun deleteAll()
}

@Dao
interface WateringDao {
    @Query("SELECT * FROM Watering")
    fun getAll(): List<Watering>
    @Insert
    fun insertAll(vararg watering: Watering)
    @Query("SELECT * FROM Watering WHERE wateringMonth = :month AND wateringYear = :year")
    suspend fun getWateringsForMonth(month: String, year: String): List<Watering>
    @Query("SELECT * FROM Watering WHERE wateringPlantID = :plantId AND wateringYear = :year AND wateringMonth = :month")
    suspend fun getWateringsID(year: String, month: String, plantId: Int): List<Watering>
    @Query("DELETE FROM Watering")
    fun deleteAll()
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
    @Query("SELECT * FROM PlantPhoto WHERE plantID IN (:plantIds)")
    suspend fun getPhotosForPlants(plantIds: List<Int>): List<PlantPhoto>
    @Query("DELETE FROM PlantPhoto")
    fun deleteAll()
}