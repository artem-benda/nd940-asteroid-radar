package com.udacity.asteroidradar.main

import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.ApiCallState
import com.udacity.asteroidradar.repository.AsteroidsRepository
import com.udacity.asteroidradar.repository.PictureOfDayRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class MainViewModel(
    private val asteroidsRepository: AsteroidsRepository,
    private val pictureOfDayRepository: PictureOfDayRepository
) : ViewModel() {
    private val _filter = MutableLiveData<AsteroidsFilterType>(AsteroidsFilterType.WEEK)

    val asteroids = Transformations.switchMap(_filter) {
        when (it) {
            AsteroidsFilterType.TODAY -> asteroidsRepository.getAsteroidsForToday()
            AsteroidsFilterType.ALL -> asteroidsRepository.getAsteroidsAll()
            else -> asteroidsRepository.getAsteroidsForWeek()
        }
    }
    val pictureOfDay = pictureOfDayRepository.getPictureOfDay()

    private val _refreshAsteroidsState = MutableLiveData<ApiCallState>()
    val refreshAsteroidsState: LiveData<ApiCallState>
        get() = _refreshAsteroidsState

    private val _refreshPictureOfDayState = MutableLiveData<ApiCallState>()
    val refreshPictureOfDayState: LiveData<ApiCallState>
        get() = _refreshPictureOfDayState

    private val _isRefreshing = MediatorLiveData<Boolean>()
    val isRefreshing: LiveData<Boolean>
        get() = _isRefreshing

    private val _navigateToDetail = MutableLiveData<Asteroid?>()
    val navigateToDetail: LiveData<Asteroid?>
        get() = _navigateToDetail

    fun onAsteroidClicked(asteroid: Asteroid) {
        _navigateToDetail.value = asteroid
    }

    fun navigateToDetailComplete() {
        _navigateToDetail.value = null
    }

    init {
        _isRefreshing.addSource(_refreshAsteroidsState) {
            _isRefreshing.value = (it is ApiCallState.Pending) && (_refreshPictureOfDayState.value is ApiCallState.Pending)
        }
        _isRefreshing.addSource(_refreshPictureOfDayState) {
            _isRefreshing.value = (it is ApiCallState.Pending) && (_refreshAsteroidsState.value is ApiCallState.Pending)
        }

        viewModelScope.launch {
            try {
                _refreshAsteroidsState.value = ApiCallState.Pending
                asteroidsRepository.refreshAsteroids()
                _refreshAsteroidsState.value = ApiCallState.Success
            } catch (e: Exception) {
                _refreshAsteroidsState.value = ApiCallState.Failure(e)
            }
        }

        viewModelScope.launch {
            try {
                _refreshPictureOfDayState.value = ApiCallState.Pending
                pictureOfDayRepository.refreshPictureOfDay()
                _refreshPictureOfDayState.value = ApiCallState.Success
            } catch (e: Exception) {
                _refreshPictureOfDayState.value = ApiCallState.Failure(e)
            }
        }
    }

    fun switchFilterType(filterType: AsteroidsFilterType) {
        _filter.value = filterType
    }
}
