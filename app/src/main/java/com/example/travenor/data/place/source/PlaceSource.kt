package com.example.travenor.data.place.source

import com.example.travenor.core.ResultListener
import com.example.travenor.data.model.photo.PlacePhoto
import com.example.travenor.data.model.place.Place

interface PlaceSource {
    interface Remote {
        fun searchExploreRestaurant(keyword: String, listener: ResultListener<List<Place>>)
        fun searchExploreHotel(keyword: String, listener: ResultListener<List<Place>>)

        fun getPlaceDetail(placeId: String, listener: ResultListener<Place>)

        fun getPlacePhoto(placeId: String, listener: ResultListener<List<PlacePhoto>>)
        fun searchExploreAttraction(
            keyword: String,
            lat: Double,
            long: Double,
            listener: ResultListener<List<Place>>
        )
    }

    interface Local {
        fun getPlaceDetail(locationId: String): Place?

        fun getPlacePhoto(locationId: String): List<PlacePhoto>?

        fun savePlaceDetail(place: Place)

        fun savePlacePhoto(placePhotos: List<PlacePhoto>)

        fun savePlaceAddress(place: Place)
    }

    interface ExplorePlaceLocal {
        /**
         * Get local explore attraction
         * @return List<Pair<String, Long>> where first is place and second is creation timestamp
         */
        fun getExploreAttractionLocal(limit: Int = 5): List<Pair<Place, Long>>

        /**
         * Get local explore restaurant
         * @return List<Pair<String, Long>> where first is place and second is creation timestamp
         */
        fun getExploreRestaurantLocal(limit: Int = 5): List<Pair<Place, Long>>

        /**
         * Get local explore hotel
         * @return List<Pair<String, Long>> where first is place and second is creation timestamp
         */
        fun getExploreHotelLocal(limit: Int = 5): List<Pair<Place, Long>>

        fun saveExploreAttractionLocal(idList: List<String>)

        fun saveExploreRestaurantLocal(idList: List<String>)

        fun saveExploreHotelLocal(idList: List<String>)
    }
}
