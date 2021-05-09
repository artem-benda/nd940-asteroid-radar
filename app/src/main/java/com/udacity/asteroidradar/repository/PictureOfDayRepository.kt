package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.dto.asPictureOfDayEntity
import com.udacity.asteroidradar.database.dao.PictureOfDayDao
import com.udacity.asteroidradar.database.entities.asDomainObject
import java.time.LocalDate

class PictureOfDayRepository(private val pictureOfDayDao: PictureOfDayDao) {

    suspend fun refreshPictureOfDay() {
        val entity = NasaApi.retrofitService
            .getApodByDate(LocalDate.now(), BuildConfig.NASA_API_KEY)
            .asPictureOfDayEntity()
        pictureOfDayDao.replacePictureOfDay(entity)
    }

    fun getPictureOfDay(): LiveData<PictureOfDay?> {
        return Transformations.map(pictureOfDayDao.findOne()) {
            it?.asDomainObject()
        }
    }
}
