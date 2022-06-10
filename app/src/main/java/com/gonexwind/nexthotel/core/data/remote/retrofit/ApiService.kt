package com.gonexwind.nexthotel.core.data.remote.retrofit

import com.gonexwind.nexthotel.core.data.remote.response.HotelsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("hotel-data")
    suspend fun getListHotel(): HotelsResponse

    @GET("hotel-data")
    suspend fun searchHotel(
        @Query("name") name: String
    ): HotelsResponse
}