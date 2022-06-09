package com.gonexwind.nexthotel.core.data.remote.retrofit

import com.gonexwind.nexthotel.core.data.remote.response.HotelsResponse
import retrofit2.http.GET

interface ApiService {
    @GET("hotel-data")
    suspend fun getListHotel(): HotelsResponse
}