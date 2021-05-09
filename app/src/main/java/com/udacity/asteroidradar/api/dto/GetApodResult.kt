package com.udacity.asteroidradar.api.dto

import com.squareup.moshi.Json
import com.udacity.asteroidradar.database.entities.PictureOfDayEntity

data class GetApodResult(
    val copyright: String,
    val date: String,
    val explanation: String,
    @Json(name = "hdurl") val hdUrl: String,
    @Json(name = "media_type") val mediaType: String,
    @Json(name = "service_version") val serviceVersion: String,
    val title: String,
    val url: String
)

fun GetApodResult.asPictureOfDayEntity(): PictureOfDayEntity {
    return PictureOfDayEntity(
        mediaType,
        title,
        url
    )
}
