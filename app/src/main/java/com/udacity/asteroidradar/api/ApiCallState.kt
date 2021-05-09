package com.udacity.asteroidradar.api

sealed class ApiCallState {
    object Pending : ApiCallState()
    object Success : ApiCallState()
    data class Failure(val exception: Exception) : ApiCallState()
}
