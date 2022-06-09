package com.gonexwind.nexthotel.core.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.gonexwind.nexthotel.core.data.local.entity.HotelEntity
import com.gonexwind.nexthotel.core.data.local.room.HotelDao
import com.gonexwind.nexthotel.core.data.remote.retrofit.ApiService

class HotelRepository private constructor(
    private val apiService: ApiService,
    private val hotelDao: HotelDao,
) {

    fun getListHotel(): LiveData<Result<List<HotelEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getListHotel()
            val hotels = response.data
            val hotelList = hotels.map {
                val isBookmarked = hotelDao.isHotelBookmarked(it.id)
                HotelEntity(
                    it.id,
                    it.name,
                    it.city,
                    it.imageUrl,
                    it.rate,
                    it.description,
                    it.priceRange,
                    isBookmarked
                )
            }
            hotelDao.deleteAll()
            hotelDao.insertHotel(hotelList)
        } catch (e: Exception) {
            Log.d("HotelRepository", "getListHotel: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getBookmarkedHotel(): LiveData<List<HotelEntity>> = hotelDao.getBookmarkedHotel()

    suspend fun setBookmarkedHotel(hotel: HotelEntity, isBookmarked: Boolean) {
        hotel.isBookmarked = isBookmarked
        hotelDao.updateHotel(hotel)
    }

    companion object {
        @Volatile
        private var instance: HotelRepository? = null

        fun getInstance(apiService: ApiService, hotelDao: HotelDao): HotelRepository {
            return instance ?: synchronized(this) {
                instance ?: HotelRepository(apiService, hotelDao)
            }.also { instance = it }
        }
    }
}