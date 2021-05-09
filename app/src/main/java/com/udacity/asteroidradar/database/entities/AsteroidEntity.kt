package com.udacity.asteroidradar.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.NasaApi
import java.time.LocalDate

@Entity(
    tableName = "asteroid",
    indices = [
        Index(value = ["close_approach_date", "codename"], name = "idx_asteroid_date", unique = false)
    ]
)
data class AsteroidEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,

    val codename: String,

    @ColumnInfo(name = "close_approach_date")
    val closeApproachDate: LocalDate,

    @ColumnInfo(name = "absolute_magnitude")
    val absoluteMagnitude: Double,

    @ColumnInfo(name = "estimated_diameter")
    val estimatedDiameter: Double,

    @ColumnInfo(name = "relative_velocity")
    val relativeVelocity: Double,

    @ColumnInfo(name = "distance_from_earth")
    val distanceFromEarth: Double,

    @ColumnInfo(name = "is_potentially_hazard")
    val isPotentiallyHazardous: Boolean
)

fun AsteroidEntity.asDomainObject(): Asteroid = Asteroid(
    id,
    codename,
    NasaApi.dateFormat.format(closeApproachDate),
    absoluteMagnitude,
    estimatedDiameter,
    relativeVelocity,
    distanceFromEarth,
    isPotentiallyHazardous
)
