package com.udacity.asteroidradar

import android.app.Application
import com.udacity.asteroidradar.worker.WorkManagerUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AsteroidRadarApp : Application() {

    private val applicationScope = CoroutineScope(Dispatchers.Default)
    private val workManagerUtils = WorkManagerUtils()

    override fun onCreate() {
        super.onCreate()
        delayedInit()
    }

    private fun delayedInit() = applicationScope.launch {
        setupRecurringWork()
    }

    private fun setupRecurringWork() {
        workManagerUtils.enqueueAll()
    }
}
