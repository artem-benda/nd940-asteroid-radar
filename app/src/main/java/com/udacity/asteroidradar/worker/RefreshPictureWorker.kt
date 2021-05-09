package com.udacity.asteroidradar.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.PictureOfDayRepository
import retrofit2.HttpException
import java.lang.Exception

class RefreshPictureWorker(private val appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        return try {
            val database = getDatabase(appContext)
            val pictureOfDayDao = database.pictureOfDayDao
            val pictureOfDayRepository = PictureOfDayRepository(pictureOfDayDao)
            pictureOfDayRepository.refreshPictureOfDay()
            Result.success()
        } catch (e: Exception) {
            if (e is HttpException) {
                Result.retry()
            } else {
                Result.failure()
            }
        }
    }

    companion object {
        const val WORK_NAME = "com.udacity.asteroidradar.worker.RefreshPictureWorker"
    }
}
