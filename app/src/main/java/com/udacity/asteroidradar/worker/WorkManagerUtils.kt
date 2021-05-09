package com.udacity.asteroidradar.worker

import android.os.Build
import androidx.work.*
import java.util.concurrent.TimeUnit

class WorkManagerUtils {

    fun enqueueAll() {
        enqueueCleanupRequest()
        enqueueRefreshAsteroidsRequest()
        enqueueRefreshPictureRequest()
    }

    private fun enqueueCleanupRequest() {
        val constraints = buildDefaultConstraints()

        val repeatingCleanupRequest = PeriodicWorkRequestBuilder<CleanupWorker>(1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance().enqueueUniquePeriodicWork(
            CleanupWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingCleanupRequest
        )
    }

    private fun enqueueRefreshAsteroidsRequest() {
        val constraints = buildDefaultConstraints()

        val repeatingRefreshAsteroidsRequest = PeriodicWorkRequestBuilder<RefreshAsteroidsWorker>(1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance().enqueueUniquePeriodicWork(
            RefreshAsteroidsWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRefreshAsteroidsRequest
        )
    }

    private fun enqueueRefreshPictureRequest() {
        val constraints = buildDefaultConstraints()

        val repeatingRefreshPictureRequest = PeriodicWorkRequestBuilder<RefreshPictureWorker>(1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance().enqueueUniquePeriodicWork(
            RefreshPictureWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRefreshPictureRequest
        )
    }

    private fun buildDefaultConstraints() = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.UNMETERED)
        .setRequiresBatteryNotLow(true)
        .setRequiresCharging(true)
        .apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setRequiresDeviceIdle(true)
            }
        }.build()
}
