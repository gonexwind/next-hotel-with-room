package com.gonexwind.nexthotel.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gonexwind.nexthotel.core.data.HotelRepository
import com.gonexwind.nexthotel.core.data.local.entity.HotelEntity
import kotlinx.coroutines.launch

class MainViewModel(private val hotelRepository: HotelRepository) : ViewModel() {
    fun searchHotel(query: String) = hotelRepository.searchHotel(query)

    fun saveHotel(hotel: HotelEntity) {
        viewModelScope.launch {
            hotelRepository.setBookmarkedHotel(hotel, true)
        }
    }

    fun deleteHotel(hotel: HotelEntity) {
        viewModelScope.launch {
            hotelRepository.setBookmarkedHotel(hotel, false)
        }
    }
}