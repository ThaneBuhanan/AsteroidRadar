/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.udacity.asteroidradar.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.nasa.gov/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val moshiRetrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

private val scalarRetrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

//https://api.nasa.gov/neo/rest/v1/feed?start_date=2015-09-07&end_date=2015-09-08&api_key=DEMO_KEY
interface AsteroidService {
    @GET("neo/rest/v1/feed")
    suspend fun getAsteroids(@Query("api_key") apiKey: String): String
}

//https://api.nasa.gov/planetary/apod?api_key=YOUR_API_KEY
interface PictureOfDayService {
    @GET("planetary/apod")
    suspend fun getPicutureOfDay(@Query("api_key") apiKey: String): String
}

object Network {
    val asteroidService: AsteroidService by lazy {
        scalarRetrofit.create(AsteroidService::class.java)
    }

    val pictureOfDayService: PictureOfDayService by lazy {
        moshiRetrofit.create(PictureOfDayService::class.java)
    }
}