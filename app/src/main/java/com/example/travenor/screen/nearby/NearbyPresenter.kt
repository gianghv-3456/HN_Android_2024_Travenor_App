package com.example.travenor.screen.nearby

import android.os.Handler
import android.os.Looper
import com.example.travenor.constant.NEARBY_DISTANCE_IN_METERS
import com.example.travenor.constant.PlaceCategory
import com.example.travenor.core.ResultListener
import com.example.travenor.data.model.location.LatLng
import com.example.travenor.data.model.photo.PlacePhoto
import com.example.travenor.data.model.place.Place
import com.example.travenor.data.repository.NearbyRepository
import com.example.travenor.data.repository.PlaceRepository
import java.util.concurrent.Callable
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors

class NearbyPresenter internal constructor(
    private val nearbyRepository: NearbyRepository,
    private val placeRepository: PlaceRepository
) : NearbyContract.Presenter {
    private var mView: NearbyContract.View? = null

    override fun setView(view: NearbyContract.View?) {
        mView = view
    }

    override fun getNearbyRestaurant(lat: Double, long: Double) {
        nearbyRepository.getNearbyRestaurant(
            LatLng(lat, long),
            NEARBY_DISTANCE_IN_METERS,
            object : ResultListener<List<Place>> {
                override fun onSuccess(data: List<Place>?) {
                    if (data.isNullOrEmpty()) {
                        return
                    }

                    // Get all place detail & photo before return to view
                    val placeIds = data.map { it.locationId }
                    fetchPlaceDetail(data, PlaceCategory.RESTAURANTS)
                }

                override fun onError(exception: Exception?) {
                    exception?.printStackTrace()
                }
            }
        )
    }

    override fun getNearbyHotel(lat: Double, long: Double) {
        nearbyRepository.getNearbyHotel(
            LatLng(lat, long),
            NEARBY_DISTANCE_IN_METERS,
            object : ResultListener<List<Place>> {
                override fun onSuccess(data: List<Place>?) {
                    if (data.isNullOrEmpty()) {
                        return
                    }

                    // Get all place detail & photo before return to view
                    val placeIds = data.map { it.locationId }
                    fetchPlaceDetail(data, PlaceCategory.HOTELS)
                }

                override fun onError(exception: Exception?) {
                    exception?.printStackTrace()
                }
            }
        )
    }

    override fun getNearbyAttraction(lat: Double, long: Double) {
        nearbyRepository.getNearbyAttraction(
            LatLng(lat, long),
            NEARBY_DISTANCE_IN_METERS,
            object : ResultListener<List<Place>> {
                override fun onSuccess(data: List<Place>?) {
                    if (data.isNullOrEmpty()) {
                        return
                    }

                    // Get all place detail & photo before return to view
                    val placeIds = data.map { it.locationId }
                    fetchPlaceDetail(data, PlaceCategory.ATTRACTIONS)
                }

                override fun onError(exception: Exception?) {
                    exception?.printStackTrace()
                }
            }
        )
    }

    /**
     * Fetch all place detail and photo and return to main ui thread
     */
    private fun fetchPlaceDetail(places: List<Place>, category: PlaceCategory) {
        val placeIds = places.map { it.locationId }

        val executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE)
        val getPhotoTaskList: List<Callable<List<PlacePhoto>>> = placePhotoGettingTaskList(placeIds)

        // Invoke all task in parallel
        executorService.execute {
            val placePhotosFuture = executorService.invokeAll(getPhotoTaskList)

            // Get result from future
            val placePhotos = mutableListOf<PlacePhoto>()
            placePhotosFuture.forEach {
                it.get()?.let { photos -> placePhotos.addAll(photos) }
            }

            // Post result to main ui thread
            Handler(Looper.getMainLooper()).post {
                when (category) {
                    PlaceCategory.RESTAURANTS -> {
                        mView?.onGetNearbyRestaurantSuccess(placePhotos, places)
                    }

                    PlaceCategory.HOTELS -> {
                        mView?.onGetNearbyHotelSuccess(placePhotos, places)
                    }

                    PlaceCategory.ATTRACTIONS -> {
                        mView?.onGetNearbyAttractionSuccess(placePhotos, places)
                    }
                }
            }

            executorService.shutdown()
        }
    }

    private fun placePhotoGettingTaskList(placeIds: List<String>): List<Callable<List<PlacePhoto>>> {
        val getPhotoTaskList = mutableListOf<Callable<List<PlacePhoto>>>()

        // Get place photo
        placeIds.forEach {
            val getPhotoTask = Callable {
                val completeFuture = CompletableFuture<List<PlacePhoto>>()
                placeRepository.getPlacePhoto(
                    it,
                    object : ResultListener<List<PlacePhoto>> {
                        override fun onSuccess(data: List<PlacePhoto>?) {
                            completeFuture.complete(data)
                        }

                        override fun onError(exception: Exception?) {
                            completeFuture.complete(null)
                        }
                    }
                )
                return@Callable completeFuture.get()
            }
            getPhotoTaskList.add(getPhotoTask)
        }
        return getPhotoTaskList
    }

    companion object {
        private const val THREAD_POOL_SIZE = 10
    }
}
