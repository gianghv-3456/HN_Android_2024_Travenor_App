package com.example.travenor.data.place.repository

import com.example.travenor.constant.PlaceCategory
import com.example.travenor.core.ResultListener
import com.example.travenor.data.model.photo.PlacePhoto
import com.example.travenor.data.model.place.Place
import com.example.travenor.data.place.source.PlaceSource

class PlaceRepositoryImpl private constructor(
    private val remote: PlaceSource.Remote,
    private val local: PlaceSource.Local,
    private val localExplore: PlaceSource.ExplorePlaceLocal
) : PlaceRepository {

    override fun searchExplorePlace(
        keyword: String,
        category: PlaceCategory,
        lat: Double,
        long: Double,
        listener: ResultListener<List<Place>>
    ) {
        // Try get online mode
        getExploreOnlineMode(
            keyword,
            category,
            lat,
            long,
            object : ResultListener<List<Place>> {
                override fun onSuccess(data: List<Place>?) {
                    // No data
                    if (data.isNullOrEmpty()) {
                        listener.onError(Exception("No data!"))
                        return
                    }
                    listener.onSuccess(data)
                }

                override fun onError(exception: Exception?) {
                    getExploreOfflineMode(category, listener)
                }
            }
        )
    }

    /**
     * Online Mode.
     * Get local then check freshness and refresh local data with api
     */
    private fun getExploreOnlineMode(
        keyword: String,
        category: PlaceCategory,
        lat: Double,
        long: Double,
        listener: ResultListener<List<Place>>
    ) {
        // Get local data and check freshness
        val result = when (category) {
            PlaceCategory.ATTRACTION -> {
                localExplore.getExploreAttractionLocal(limit = 5)
            }

            PlaceCategory.RESTAURANT -> {
                localExplore.getExploreRestaurantLocal(limit = 5)
            }

            PlaceCategory.HOTEL -> {
                localExplore.getExploreHotelLocal(limit = 5)
            }
        }

        // No data stored, so get remote
        if (result.isEmpty()) {
            getExplorePlaceFromRemote(keyword, lat, long, category, listener)
            return
        }

        val samplePair = result[0]
        val timeStamp = samplePair.second

        // Data is fresh, so return local data
        if (isDataExploreFresh(timeStamp)) {
            listener.onSuccess(
                result.map {
                    it.first
                }
            )
        } else {
            // Get remote data from api and store to local
            getExplorePlaceFromRemote(keyword, lat, long, category, listener)
        }
    }

    /**
     * Offline Mode.
     * Get local data only
     */
    private fun getExploreOfflineMode(
        category: PlaceCategory,
        listener: ResultListener<List<Place>>
    ) {
        // Get local data and check freshness
        val result = when (category) {
            PlaceCategory.ATTRACTION -> {
                localExplore.getExploreAttractionLocal(limit = 5)
            }

            PlaceCategory.RESTAURANT -> {
                localExplore.getExploreRestaurantLocal(limit = 5)
            }

            PlaceCategory.HOTEL -> {
                localExplore.getExploreHotelLocal(limit = 5)
            }
        }

        if (result.isEmpty()) {
            listener.onError(Exception("No data!"))
        } else {
            listener.onSuccess(
                result.map {
                    it.first
                }
            )
        }
    }

    /**
     * Data explore fresh if it has been created within the previous 2 days ago
     */
    private fun isDataExploreFresh(timeStamp: Long): Boolean {
        // Calculate the current time minus the time the data was last updated
        val timeSinceLastUpdate = System.currentTimeMillis() - timeStamp

        // Return true if the time since the last update is less than the freshness threshold
        val result = timeSinceLastUpdate < DATA_FRESHNESS_THRESH_HOLD
        return result
    }

    private fun getExplorePlaceFromRemote(
        keyword: String,
        lat: Double,
        long: Double,
        category: PlaceCategory,
        listener: ResultListener<List<Place>>
    ) {
        when (category) {
            PlaceCategory.ATTRACTION ->
                getExploreAttractionRemote(keyword, lat, long, listener)

            PlaceCategory.RESTAURANT ->
                getExploreRestaurantRemote(keyword, lat, long, listener)

            PlaceCategory.HOTEL ->
                getExploreHotelRemote(keyword, lat, long, listener)
        }
    }

    private fun getExploreAttractionRemote(
        keyword: String,
        lat: Double,
        long: Double,
        listener: ResultListener<List<Place>>
    ) {
        remote.searchExploreAttraction(
            keyword,
            lat,
            long,
            object : ResultListener<List<Place>> {
                override fun onSuccess(data: List<Place>?) {
                    if (data.isNullOrEmpty()) {
                        listener.onError(Exception("No data!"))
                        return
                    }

                    listener.onSuccess(data)

                    // Save to local DB
                    localExplore.saveExploreAttractionLocal(data.map { it.locationId })
                    data.forEach {
                        local.savePlaceDetail(it)
                        local.savePlaceAddress(it)
                    }
                }

                override fun onError(exception: Exception?) {
                    listener.onError(exception)
                }
            }
        )
    }

    private fun getExploreRestaurantRemote(
        keyword: String,
        lat: Double,
        long: Double,
        listener: ResultListener<List<Place>>
    ) {
        remote.searchExploreRestaurant(
            keyword,
            lat,
            long,
            object : ResultListener<List<Place>> {
                override fun onSuccess(data: List<Place>?) {
                    // No data
                    if (data.isNullOrEmpty()) {
                        listener.onError(Exception("No data!"))
                        return
                    }

                    listener.onSuccess(data)

                    // Save to local DB
                    localExplore.saveExploreRestaurantLocal(data.map { it.locationId })
                    data.forEach {
                        local.savePlaceDetail(it)
                        local.savePlaceAddress(it)
                    }
                }

                override fun onError(exception: Exception?) {
                    listener.onError(exception)
                }
            }
        )
    }

    private fun getExploreHotelRemote(
        keyword: String,
        lat: Double,
        long: Double,
        listener: ResultListener<List<Place>>
    ) {
        remote.searchExploreHotel(
            keyword,
            lat,
            long,
            object : ResultListener<List<Place>> {
                override fun onSuccess(data: List<Place>?) {
                    if (data.isNullOrEmpty()) {
                        listener.onError(Exception("No data!"))
                        return
                    }

                    listener.onSuccess(data)
                    // Save to local DB
                    localExplore.saveExploreHotelLocal(data.map { it.locationId })
                    data.forEach {
                        local.savePlaceDetail(it)
                        local.savePlaceAddress(it)
                    }
                }

                override fun onError(exception: Exception?) {
                    listener.onError(exception)
                }
            }
        )
    }

    override fun getPlaceDetail(placeId: String, listener: ResultListener<Place>) {
        // Online mode

        remote.getPlaceDetail(
            placeId,
            object : ResultListener<Place> {
                override fun onSuccess(data: Place?) {
                    if (data == null) {
                        listener.onError(Exception("No data!"))
                        return
                    }
                    listener.onSuccess(data)

                    // Save to local DB
                    local.savePlaceDetail(data)
                }

                override fun onError(exception: Exception?) {
                    exception?.printStackTrace()
                    local.getPlaceDetail(placeId).apply {
                        if (this == null) {
                            listener.onError(Exception("No data!"))
                        } else {
                            listener.onSuccess(this)
                        }
                    }
                }
            }
        )
    }

    override fun getPlacePhoto(placeId: String, listener: ResultListener<List<PlacePhoto>>) {
        remote.getPlacePhoto(
            placeId,
            object : ResultListener<List<PlacePhoto>> {
                override fun onSuccess(data: List<PlacePhoto>?) {
                    if (data.isNullOrEmpty()) {
                        listener.onError(Exception("No data!"))
                        return
                    }
                    data.forEach {
                        it.locationId = placeId
                    }
                    listener.onSuccess(data)

                    // Save to local DB
                    local.savePlacePhoto(data)
                }

                override fun onError(exception: Exception?) {
                    exception?.printStackTrace()
                    local.getPlacePhoto(placeId).apply {
                        if (this.isNullOrEmpty()) {
                            listener.onError(Exception("No data!"))
                        } else {
                            listener.onSuccess(this)
                        }
                    }
                }
            }
        )
    }

    companion object {
        private const val DATA_FRESHNESS_THRESH_HOLD = 2 * 24 * 60 * 60 * 1000 // 2 days
        private var instance: PlaceRepositoryImpl? = null

        fun getInstance(
            remote: PlaceSource.Remote,
            local: PlaceSource.Local,
            localExplore: PlaceSource.ExplorePlaceLocal
        ) =
            synchronized(this) {
                instance ?: PlaceRepositoryImpl(
                    remote,
                    local,
                    localExplore
                ).also { instance = it }
            }
    }
}
