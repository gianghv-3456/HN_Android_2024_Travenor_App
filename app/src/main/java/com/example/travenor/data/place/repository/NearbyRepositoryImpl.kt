package com.example.travenor.data.place.repository

import com.example.travenor.constant.PlaceCategory
import com.example.travenor.core.ResultListener
import com.example.travenor.data.model.place.Place
import com.example.travenor.data.place.source.PlaceSource

class NearbyRepositoryImpl(
    private val remote: PlaceSource.Remote,
    private val local: PlaceSource.Local,
) : NearbyRepository {
    override fun getNearbyRestaurant(
        lat: Double, long: Double, radiusInMeters: Double, listener: ResultListener<List<Place>>
    ) {
        // Try get from remote
        remote.getNearbyPlace(lat,
            long,
            limit = 10,
            category = PlaceCategory.RESTAURANTS,
            radius = radiusInMeters,
            object : ResultListener<List<Place>> {
                override fun onSuccess(data: List<Place>?) {
                    if (data.isNullOrEmpty()) {
                        // Try get from local
                        val result: List<Place>? = local.getNearbyPlaceLocal(
                            lat, long, category = PlaceCategory.RESTAURANTS, radius = radiusInMeters
                        )

                        if (result.isNullOrEmpty()) listener.onError(Exception("No data"))
                        else listener.onSuccess(result)
                    }

                    listener.onSuccess(data)
                }

                override fun onError(exception: Exception?) {
                    listener.onError(exception)
                }

            })
    }

    override fun getNearbyHotel(
        lat: Double, long: Double, radiusInMeters: Double, listener: ResultListener<List<Place>>
    ) {
        // Try get from remote
        remote.getNearbyPlace(lat,
            long,
            limit = 10,
            category = PlaceCategory.HOTELS,
            radius = radiusInMeters,
            object : ResultListener<List<Place>> {
                override fun onSuccess(data: List<Place>?) {
                    if (data.isNullOrEmpty()) {
                        // Try get from local
                        val result: List<Place>? = local.getNearbyPlaceLocal(
                            lat, long, category = PlaceCategory.RESTAURANTS, radius = radiusInMeters
                        )

                        if (result.isNullOrEmpty()) listener.onError(Exception("No data"))
                        else listener.onSuccess(result)
                    }

                    listener.onSuccess(data)
                }

                override fun onError(exception: Exception?) {
                    listener.onError(exception)
                }

            })
    }

    override fun getNearbyAttraction(
        lat: Double, long: Double, radiusInMeters: Double, listener: ResultListener<List<Place>>
    ) {
        // Try get from remote
        remote.getNearbyPlace(lat,
            long,
            limit = 10,
            category = PlaceCategory.ATTRACTIONS,
            radius = radiusInMeters,
            object : ResultListener<List<Place>> {
                override fun onSuccess(data: List<Place>?) {
                    if (data.isNullOrEmpty()) {
                        // Try get from local
                        val result: List<Place>? = local.getNearbyPlaceLocal(
                            lat, long, category = PlaceCategory.RESTAURANTS, radius = radiusInMeters
                        )

                        if (result.isNullOrEmpty()) listener.onError(Exception("No data"))
                        else listener.onSuccess(result)
                    }

                    listener.onSuccess(data)
                }

                override fun onError(exception: Exception?) {
                    listener.onError(exception)
                }

            })
    }

    companion object {
        private var instance: NearbyRepositoryImpl? = null

        fun getInstance(
            remote: PlaceSource.Remote, local: PlaceSource.Local
        ) = synchronized(this) {
            instance ?: NearbyRepositoryImpl(
                remote, local
            ).also { instance = it }
        }
    }
}