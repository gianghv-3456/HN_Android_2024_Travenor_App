package com.example.travenor.data.repository

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

    fun getRecentSearch(listener: ResultListener<List<String>>)

    fun saveRecentSearch(keyword: List<String>)

    fun searchPlace(
        keyword: String,
        category: PlaceCategory?,
        listener: ResultListener<List<Place>>
    )

    fun getPlaceDetail(placeId: String, listener: ResultListener<Place>)

    fun getPlacePhoto(placeId: String, listener: ResultListener<List<PlacePhoto>>)

    fun markFavorite(placeId: String, listener: ResultListener<Boolean>)

    fun unmarkFavorite(placeId: String, listener: ResultListener<Boolean>)

    fun getFavoritePlace(listener: ResultListener<List<Place>>)
}
