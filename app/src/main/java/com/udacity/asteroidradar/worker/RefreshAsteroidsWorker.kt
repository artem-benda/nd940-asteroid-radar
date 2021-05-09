package com.udacity.asteroidradar.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidsRepository
import retrofit2.HttpException
import java.lang.Exception

class RefreshAsteroidsWorker(private val appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        return try {
            val database = getDatabase(appContext)
            val asteroidsDao = database.asteroidsDao
            val asteroidsRepository = AsteroidsRepository(asteroidsDao)
            asteroidsRepository.refreshAsteroids()
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
        const val WORK_NAME = "com.udacity.asteroidradar.worker.RefreshAsteroidsWorker"
    }
}
