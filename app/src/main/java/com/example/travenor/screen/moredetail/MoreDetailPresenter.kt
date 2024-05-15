package com.example.travenor.screen.moredetail

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

class MoreDetailPresenter internal constructor(
    private val placeRepository: PlaceRepository,
    private val nearbyRepository: NearbyRepository
) : MoreDetailContract.Presenter {
    private var mView: MoreDetailContract.View? = null
    override fun getPlaceDetail(locationId: String) {
        placeRepository.getPlaceDetail(
            locationId,
            object : ResultListener<Place> {
                override fun onSuccess(data: Place?) {
                    if (data != null) {
                        mView?.onGetPlaceDetailSuccess(data)
                    } else {
                        mView?.onGetPlaceDetailFail(Exception("Data null when get place detail"))
                    }
                }

                override fun onError(exception: Exception?) {
                    exception?.printStackTrace()
                    mView?.onGetPlaceDetailFail(exception!!)
                }
            }
        )
    }

    override fun markFavorite(placeId: String, isFavorite: Boolean) {
        // Toggle favorite
        if (isFavorite) {
            placeRepository.markFavorite(
                placeId,
                object : ResultListener<Boolean> {
                    override fun onSuccess(data: Boolean?) {
                        if (data == true) mView?.onMarkFavoriteSuccess()
                    }

                    override fun onError(exception: Exception?) {
                        exception?.printStackTrace()/* no-op */
                    }
                }
            )
        } else {
            placeRepository.markNotFavorite(
                placeId,
                object : ResultListener<Boolean> {
                    override fun onSuccess(data: Boolean?) {
                        if (data == true) mView?.onMarkNotFavoriteSuccess()
                    }

                    override fun onError(exception: Exception?) {
                        exception?.printStackTrace()/* no-op */
                    }
                }
            )
        }
    }

    override fun getPlacePhotos(locationId: String) {
        placeRepository.getPlacePhoto(
            locationId,
            object : ResultListener<List<PlacePhoto>> {
                override fun onSuccess(data: List<PlacePhoto>?) {
                    if (data.isNullOrEmpty()) return

                    for (photo in data) photo.locationId = locationId

                    mView?.onGetPhotoSuccess(data)
                }

                override fun onError(exception: Exception?) { /* no-op */
                }
            }
        )
    }

    override fun getNearbyRestaurant(lat: Double, long: Double) {
        nearbyRepository.getNearbyRestaurant(
            LatLng(lat, long),
            NEARBY_DISTANCE_IN_METERS,
            object : ResultListener<List<Place>> {
                override fun onSuccess(data: List<Place>?) {
                    if (!data.isNullOrEmpty()) {
                        val idList = mutableListOf<String>()
                        for (place in data) {
                            getNearbyPlacePhoto(place.locationId, PlaceCategory.RESTAURANTS)

                            // Get detail for each place
                            idList.add(place.locationId)
                        }
                        getNearbyPlaceDetail(idList, PlaceCategory.RESTAURANTS)
                    }
                }

                override fun onError(exception: Exception?) {
                    exception?.run { printStackTrace() }
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
                    if (!data.isNullOrEmpty()) {
                        val idList = mutableListOf<String>()
                        for (place in data) {
                            getNearbyPlacePhoto(place.locationId, PlaceCategory.HOTELS)

                            // Get detail for each place
                            idList.add(place.locationId)
                        }
                        getNearbyPlaceDetail(idList, PlaceCategory.HOTELS)
                    }
                }

                override fun onError(exception: Exception?) {
                    exception?.printStackTrace()
                }
            }
        )
    }

    /**
     * get nearby func only return placeId so need to call getPlaceDetail and getPlacePhoto
     * for all placeId then return to view
     * To handle it, using Callable and FutureTask list
     */
    private fun getNearbyPlaceDetail(ids: List<String>, category: PlaceCategory) {
        val executor = Executors.newFixedThreadPool(MAX_THREAD_POOL_SIZE)
        val listFuture = mutableListOf<Callable<Place>>()

        for (id in ids) {
            val callable = Callable {
                val future = CompletableFuture<Place>()

                placeRepository.getPlaceDetail(
                    id,
                    object : ResultListener<Place> {
                        override fun onSuccess(data: Place?) {
                            if (data != null) {
                                future.complete(data)
                            } else {
                                future.complete(null)
                            }
                        }

                        override fun onError(exception: Exception?) { /* no-op */
                            future.complete(null)
                        }
                    }
                )
                return@Callable future.get()
            }
            listFuture.add(callable)
        }

        executor.execute {
            val futureList = executor.invokeAll(listFuture)
            val result = mutableListOf<Place>()
            futureList.forEach {
                it.get()?.let { it1 -> result.add(it1) }
            }

            // Post result to main ui thread
            Handler(Looper.getMainLooper()).post {
                if (result.isNotEmpty()) {
                    when (category) {
                        PlaceCategory.RESTAURANTS -> mView?.onGetNearbyRestaurantSuccess(result)
                        PlaceCategory.HOTELS -> mView?.onGetNearbyHotelSuccess(result)
                        else -> { /* no-op */
                        }
                    }
                }
            }
            executor.shutdown()
        }
    }

    override fun getNearbyPlacePhoto(locationId: String, category: PlaceCategory) {
        placeRepository.getPlacePhoto(
            locationId,
            object : ResultListener<List<PlacePhoto>> {
                override fun onSuccess(data: List<PlacePhoto>?) {
                    if (data.isNullOrEmpty()) return

                    for (photo in data) photo.locationId = locationId
                    mView?.onGetNearbyPlacePhotoSuccess(data, category)
                }

                override fun onError(exception: Exception?) { /* no-op */
                }
            }
        )
    }

    override fun onStart() { /* no-op */
    }

    override fun onStop() { /* no-op */
    }

    override fun setView(view: MoreDetailContract.View?) {
        mView = view
    }

    companion object {
        private const val MAX_THREAD_POOL_SIZE = 5
    }
}
