package com.archi.orchard

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.*
import java.time.LocalDate


@Entity(tableName="garden")
public final class Plant() {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
    @ColumnInfo(name = "name") var name: String = ""
    @ColumnInfo(name = "note") var note: String = ""
    @ColumnInfo(name = "imagepath") var imagepath: String = ""
    @ColumnInfo(name = "nextdate") var nextdate: String = ""
    @ColumnInfo(name = "watering") var watering: Int = 0


    constructor(name: String, note: String, imagepath: String, nextdate: String, watering: Int ): this() {

        this.name = name
        this.note = note
        this.imagepath = imagepath
        this.nextdate = nextdate
        this.watering = watering

    }

}

@Entity(tableName="history")
public final class Wateringdate() {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
    @ColumnInfo(name = "plantid") var plantid: Int = 0
    @RequiresApi(Build.VERSION_CODES.O)
    @ColumnInfo(name = "date") var date: String = LocalDate.now().toString()
    @RequiresApi(Build.VERSION_CODES.O)
    constructor(plantid: Int, date: String): this() {

        this.plantid = plantid
        this.date = date

    }

}

@Database(entities = [Plant::class, Wateringdate::class], version = 1)


abstract class AppDatabase : RoomDatabase() {
    abstract fun gardenDao(): GardenDao
}
