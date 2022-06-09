package com.gonexwind.nexthotel.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hotel")
data class HotelEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val city: String,
    val imageUrl: String,
    val rate: String,
    val description: String,
    val priceRange: String,
    var isBookmarked: Boolean,
)