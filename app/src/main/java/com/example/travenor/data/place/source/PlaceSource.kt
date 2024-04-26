package com.example.travenor.data.place.source

import com.example.travenor.core.ResultListener
import com.example.travenor.data.model.photo.PlacePhoto
import com.example.travenor.data.model.place.Place

interface PlaceSource {
    interface Remote {
        fun searchExploreAttraction(keyword: String, listener: ResultListener<List<Place>>)
        fun searchExploreRestaurant(keyword: String, listener: ResultListener<List<Place>>)
        fun searchExploreHotel(keyword: String, listener: ResultListener<List<Place>>)

        fun getPlacePhoto(placeId: String, listener: ResultListener<List<PlacePhoto>>)
    }
}
