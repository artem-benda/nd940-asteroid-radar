package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants.BASE_URL
import com.udacity.asteroidradar.api.dto.FindAsteroidsResult
import com.udacity.asteroidradar.api.dto.GetApodResult
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

interface NasaApiService {
    @GET("neo/rest/v1/feed")
    suspend fun findAsteroidsByDateInterval(
        @Query("start_date") startDate: LocalDate,
        @Query("end_date") endDate: LocalDate?,
        @Query("api_key") apiKey: String
    ): FindAsteroidsResult

    @GET("planetary/apod")
    suspend fun getApodByDate(
        @Query("date") date: LocalDate?,
        @Query("api_key") apiKey: String?
    ): GetApodResult
}

object NasaApi {
    val retrofitService: NasaApiService by lazy {
        retrofit.create(NasaApiService::class.java)
    }

    val dateFormat = DateTimeFormatter.ISO_LOCAL_DATE
}
