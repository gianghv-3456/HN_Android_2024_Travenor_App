package com.example.travenor.data.place.repository

import com.example.travenor.constant.PlaceCategory
import com.example.travenor.core.ResultListener
import com.example.travenor.data.model.photo.PlacePhoto
import com.example.travenor.data.model.place.Place

interface PlaceRepository {

    fun searchExplorePlace(
        keyword: String,
        category: PlaceCategory,
        lat: Double,
        long: Double,
        listener: ResultListener<List<Place>>
    )

    fun getPlaceDetail(placeId: String, listener: ResultListener<Place>)

    fun getPlacePhoto(placeId: String, listener: ResultListener<List<PlacePhoto>>)
}
