package com.udacity.asteroidradar.database

import android.content.Context
import androidx.room.*
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.database.dao.AsteroidsDao
import com.udacity.asteroidradar.database.dao.PictureOfDayDao
import com.udacity.asteroidradar.database.entities.AsteroidEntity
import com.udacity.asteroidradar.database.entities.PictureOfDayEntity
import java.time.LocalDate

@Database(entities = [AsteroidEntity::class, PictureOfDayEntity::class], version = 1)
@TypeConverters(LocalDateTimeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val asteroidsDao: AsteroidsDao
    abstract val pictureOfDayDao: PictureOfDayDao
}

private lateinit var INSTANCE: AppDatabase

fun getDatabase(context: Context): AppDatabase {
    synchronized(AppDatabase::class) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context,
                AppDatabase::class.java, "app-database"
            ).build()
        }
        return INSTANCE
    }
}

object LocalDateTimeConverter {
    @TypeConverter
    fun toDate(dateString: String?): LocalDate? {
        return if (dateString == null) {
            null
        } else {
            LocalDate.parse(dateString, NasaApi.dateFormat)
        }
    }

    @TypeConverter
    fun toDateString(date: LocalDate?): String? {
        return if (date == null) {
            null
        } else {
            NasaApi.dateFormat.format(date)
        }
    }
}
