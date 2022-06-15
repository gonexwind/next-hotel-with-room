package com.nexthotel.core.data.remote.network

import com.nexthotel.core.data.remote.response.HotelsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("list-hotels")
    suspend fun getExploreHotel(): HotelsResponse

    @GET("hotel-data/hotel-bestpicks")
    suspend fun getBestPicksHotels(): HotelsResponse

    @GET("hotel-data/list-hotels")
    suspend fun getRecommendationHotels(): HotelsResponse

    @GET("list-hotels")
    suspend fun searchHotel(
        @Query("name") name: String
    ): Response<HotelsResponse>
}