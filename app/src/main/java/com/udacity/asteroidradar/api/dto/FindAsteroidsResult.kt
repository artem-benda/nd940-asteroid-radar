package com.udacity.asteroidradar.api.dto

import com.squareup.moshi.Json
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.database.entities.AsteroidEntity
import java.time.LocalDate

data class FindAsteroidsResult(
    @Json(name = "near_earth_objects") val nearEarthObjects: Map<String, List<NearEarthObjectDto>>
)

data class NearEarthObjectDto(
    val id: String,
    val name: String,
    @Json(name = "absolute_magnitude_h") val absoluteMagnitudeH: Double,
    @Json(name = "estimated_diameter") val estimatedDiameter: EstimatedDiameterDto,
    @Json(name = "is_potentially_hazardous_asteroid") val isPotentiallyHazardous: Boolean,
    @Json(name = "close_approach_data") val closeApproachData: List<CloseApproachDto>,
    @Json(name = "is_sentry_object") val isSentryObject: Boolean
)

data class EstimatedDiameterDto(
    val kilometers: EstimatedDiameterRangeDto,
    val meters: EstimatedDiameterRangeDto,
    val miles: EstimatedDiameterRangeDto,
    val feet: EstimatedDiameterRangeDto
)

data class EstimatedDiameterRangeDto(
    @Json(name = "estimated_diameter_min") val estimatedDiameterMin: Double,
    @Json(name = "estimated_diameter_max") val estimatedDiameterMax: Double
)

data class CloseApproachDto(
    @Json(name = "close_approach_date") val closeApproachDate: String,
    @Json(name = "close_approach_date_full") val closeApproachDateTime: String,
    @Json(name = "epoch_date_close_approach") val epochDateCloseApproach: Long,
    @Json(name = "relative_velocity") val relativeVelocity: VelocityDto,
    @Json(name = "miss_distance") val missDistance: MissDistanceDto,
    @Json(name = "orbiting_body") val orbitingBody: String
)

data class VelocityDto(
    @Json(name = "kilometers_per_second") val kilometersPerSecond: String,
    @Json(name = "kilometers_per_hour") val kilometersPerHour: String,
    @Json(name = "miles_per_hour") val milesPerHour: String
)

data class MissDistanceDto(
    val astronomical: String,
    val lunar: String,
    val kilometers: String,
    val miles: String
)

fun FindAsteroidsResult.asAsteroidEntitiesList(): List<AsteroidEntity> {
    return nearEarthObjects.values
        .flatten()
        .filter { !it.closeApproachData.isNullOrEmpty() }
        .map {
            AsteroidEntity(
                it.id.toLong(),
                it.name,
                LocalDate.parse(it.closeApproachData[0].closeApproachDate, NasaApi.dateFormat),
                it.absoluteMagnitudeH,
                it.estimatedDiameter.kilometers.estimatedDiameterMax,
                it.closeApproachData[0].relativeVelocity.kilometersPerSecond.toDouble(),
                it.closeApproachData[0].missDistance.astronomical.toDouble(),
                it.isPotentiallyHazardous
            )
        }
}
