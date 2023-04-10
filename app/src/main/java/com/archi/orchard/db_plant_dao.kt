package com.archi.orchard

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import java.time.LocalDate

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

    @Query("SELECT nextdate FROM garden")
    fun listOfNextDays(): Array<String>

    @Query("SELECT watering FROM garden WHERE name IN (:plant_name)")
    fun waterById(plant_name: String): Int

    /*
    @Query("SELECT name FROM garden WHERE nextdate IN (:today)")
    fun todayNames(today: String): List<Plant>

    @Query("SELECT nextdate FROM garden WHERE nextdate IN (:today)")
    fun todayDays(today: String): List<Plant>

     */

    @Query("SELECT date FROM history WHERE plantid IN (:users_plant_id)")
    fun historyById(users_plant_id: Int): List<String>

    @Query("INSERT INTO history (plantid, date) VALUES (:plantid, :date)")
    fun addDate(plantid: Int, date: String)

}
