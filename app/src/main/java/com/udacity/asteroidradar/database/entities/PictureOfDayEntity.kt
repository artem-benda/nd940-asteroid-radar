package com.udacity.asteroidradar.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.PictureOfDay

@Entity(tableName = "picture_of_the_day")
data class PictureOfDayEntity(

    @ColumnInfo(name = "media_type")
    val mediaType: String,

    val title: String,

    @PrimaryKey
    val url: String,
)

fun PictureOfDayEntity.asDomainObject(): PictureOfDay = PictureOfDay(
    mediaType,
    title,
    url
)
