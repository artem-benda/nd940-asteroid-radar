package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.dto.asAsteroidEntitiesList
import com.udacity.asteroidradar.database.dao.AsteroidsDao
import com.udacity.asteroidradar.database.entities.asDomainObject
import java.time.LocalDate

class AsteroidsRepository(private val asteroidsDao: AsteroidsDao) {

    suspend fun refreshAsteroids() {
        val entities = NasaApi.retrofitService
            .findAsteroidsByDateInterval(LocalDate.now(), null, BuildConfig.NASA_API_KEY)
            .asAsteroidEntitiesList()
        asteroidsDao.insertAll(entities)
    }

    fun getAsteroidsForWeek(): LiveData<List<Asteroid>> {
        return Transformations.map(asteroidsDao.findByRangeDate(LocalDate.now(), LocalDate.now().plusDays(6))) { entities ->
            entities.map { it.asDomainObject() }
        }
    }

    fun getAsteroidsForToday(): LiveData<List<Asteroid>> {
        return Transformations.map(asteroidsDao.findByRangeDate(LocalDate.now(), LocalDate.now())) { entities ->
            entities.map { it.asDomainObject() }
        }
    }

    fun getAsteroidsAll(): LiveData<List<Asteroid>> {
        return Transformations.map(asteroidsDao.findAll()) { entities ->
            entities.map { it.asDomainObject() }
        }
    }

    suspend fun cleanupOld() {
        asteroidsDao.deleteBeforeDate(LocalDate.now())
    }
}
