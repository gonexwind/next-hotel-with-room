package com.gonexwind.nexthotel.core.di

import android.content.Context
import com.gonexwind.nexthotel.core.data.HotelRepository
import com.gonexwind.nexthotel.core.data.local.room.HotelDatabase
import com.gonexwind.nexthotel.core.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): HotelRepository {
        val apiService = ApiConfig.getApiService()
        val database = HotelDatabase.getInstance(context)
        val dao = database.hotelDao()
        return HotelRepository.getInstance(apiService, dao)
    }
}