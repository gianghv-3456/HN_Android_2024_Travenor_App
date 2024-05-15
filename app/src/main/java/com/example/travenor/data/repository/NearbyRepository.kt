package com.example.travenor.data.repository

import com.example.travenor.core.ResultListener
import com.example.travenor.data.model.location.LatLng
import com.example.travenor.data.model.place.Place

interface NearbyRepository {
    fun getNearbyRestaurant(
        latLng: LatLng,
        radiusInMeters: Double,
        listener: ResultListener<List<Place>>
    )

    fun getNearbyHotel(
        latLng: LatLng,
        radiusInMeters: Double,
        listener: ResultListener<List<Place>>
    )

    fun getNearbyAttraction(
        latLng: LatLng,
        radiusInMeters: Double,
        listener: ResultListener<List<Place>>
    )
}
