package com.udacity.asteroidradar.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.database.entities.AsteroidEntity
import java.time.LocalDate

@Dao
interface AsteroidsDao {
    @Query("select * from asteroid order by close_approach_date, codename")
    fun findAll(): LiveData<List<AsteroidEntity>>

    @Query("select * from asteroid where close_approach_date >= :dateStart and close_approach_date <= :dateEnd order by close_approach_date, codename")
    fun findByRangeDate(dateStart: LocalDate, dateEnd: LocalDate): LiveData<List<AsteroidEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(asteroids: List<AsteroidEntity>)

    @Query("delete from asteroid where close_approach_date < :date")
    suspend fun deleteBeforeDate(date: LocalDate)
}
