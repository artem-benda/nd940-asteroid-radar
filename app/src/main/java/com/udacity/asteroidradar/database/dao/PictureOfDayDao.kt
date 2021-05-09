package com.udacity.asteroidradar.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.database.entities.PictureOfDayEntity

@Dao
abstract class PictureOfDayDao {
    @Query("delete from picture_of_the_day")
    abstract suspend fun clearTable()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun addPictureOfDay(pictureOfDayEntity: PictureOfDayEntity)

    @Transaction
    open suspend fun replacePictureOfDay(pictureOfDayEntity: PictureOfDayEntity) {
        clearTable()
        addPictureOfDay(pictureOfDayEntity)
    }

    @Query("select * from picture_of_the_day limit 1")
    abstract fun findOne(): LiveData<PictureOfDayEntity?>
}
