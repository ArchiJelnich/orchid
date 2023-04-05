package com.archi.orchard

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface GardenDao {
    @Query("SELECT * FROM garden")
    fun getAll(): List<Plant>

    @Insert
    fun insertAll(vararg calendar: Plant)

    @Delete
    fun delete(calendar: Plant)

    @Query("DELETE FROM Garden")
    fun deleteAll()

    @Query("SELECT COUNT(*) FROM garden")
    fun getCount(): Int

    @Query("SELECT name FROM garden")
    fun listOfNames(): Array<String>

    @Query("SELECT note FROM garden")
    fun listOfNotes(): Array<String>

    @Query("SELECT watering FROM garden")
    fun listOfWateringTypes(): Array<Int>


}
