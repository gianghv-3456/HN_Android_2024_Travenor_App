package com.example.travenor.data.place.source

import com.example.travenor.core.ResultListener
import com.example.travenor.data.model.photo.PlacePhoto
import com.example.travenor.data.model.place.Place

interface PlaceSource {
    interface Remote {
        fun searchExploreAttraction(listener: ResultListener<List<Place>>)
        fun searchExploreRestaurant(listener: ResultListener<List<Place>>)
        fun searchExploreHotel(listener: ResultListener<List<Place>>)

        fun getPlacePhoto(placeId: String, listener: ResultListener<List<PlacePhoto>>)
    }
}
