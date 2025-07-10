package com.example.orchid.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Plant::class, Watering::class, PlantPhoto::class],version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun PlantDao(): PlantDao
    abstract fun WateringDao(): WateringDao
    abstract fun PlantPhotoDao(): PlantPhotoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null


        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "orchid_app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }


    }


}