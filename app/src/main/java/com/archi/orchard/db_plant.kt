package com.archi.orchard

import androidx.room.*

@Entity(tableName="garden")
public class Plant() {
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

@Database(entities = [Plant::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gardenDao(): GardenDao
}
