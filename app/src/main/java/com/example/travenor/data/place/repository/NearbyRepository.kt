package com.example.travenor.data.place.repository

import com.example.travenor.core.ResultListener
import com.example.travenor.data.model.place.Place

interface NearbyRepository {
    fun getNearbyRestaurant(
        lat: Double,
        long: Double,
        radiusInMeters: Double,
        listener: ResultListener<List<Place>>
    )

    fun getNearbyHotel(
        lat: Double,
        long: Double,
        radiusInMeters: Double,
        listener: ResultListener<List<Place>>
    )

    fun getNearbyAttraction(
        lat: Double,
        long: Double,
        radiusInMeters: Double,
        listener: ResultListener<List<Place>>
    )
}
