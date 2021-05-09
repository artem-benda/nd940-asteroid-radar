package com.udacity.asteroidradar.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.udacity.asteroidradar.repository.AsteroidsRepository
import com.udacity.asteroidradar.repository.PictureOfDayRepository

class MainViewModelFactory(
    private val asteroidsRepository: AsteroidsRepository,
    private val pictureOfDayRepository: PictureOfDayRepository
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(asteroidsRepository, pictureOfDayRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
