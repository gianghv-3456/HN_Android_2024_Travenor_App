package com.example.travenor.data.place.repository

import com.example.travenor.core.ResultListener
import com.example.travenor.data.model.photo.PlacePhoto
import com.example.travenor.data.model.place.Place
import com.example.travenor.data.place.source.PlaceSource

class PlaceRepository private constructor(
    private val remote: PlaceSource.Remote
) : PlaceSource.Remote {
    override fun searchExploreAttraction(listener: ResultListener<List<Place>>) {
        // TODO Implement Local data
        remote.searchExploreAttraction(listener)
    }

    override fun searchExploreRestaurant(listener: ResultListener<List<Place>>) {
//        TODO Implement Local Data
        remote.searchExploreRestaurant(listener)
    }

    override fun searchExploreHotel(listener: ResultListener<List<Place>>) {
        // TODO Implement Local Data
        remote.searchExploreHotel(listener)
    }

    override fun getPlacePhoto(placeId: String, listener: ResultListener<List<PlacePhoto>>) {
        remote.getPlacePhoto(placeId, listener)
    }

    companion object {
        private var instance: PlaceRepository? = null

        fun getInstance(local: PlaceSource.Remote) = synchronized(this) {
            instance ?: PlaceRepository(local).also { instance = it }
        }
    }
}
