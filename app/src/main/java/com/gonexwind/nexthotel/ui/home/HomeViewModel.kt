package com.gonexwind.nexthotel.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gonexwind.nexthotel.core.data.HotelRepository
import com.gonexwind.nexthotel.core.data.local.entity.HotelEntity
import kotlinx.coroutines.launch

class HomeViewModel(private val hotelRepository: HotelRepository) : ViewModel() {
    fun getListHotel() = hotelRepository.getListHotel()

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