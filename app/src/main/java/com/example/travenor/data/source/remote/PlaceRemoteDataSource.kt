package com.example.travenor.data.source.remote

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.travenor.BuildConfig.TRIP_ADVISOR_KEY
import com.example.travenor.constant.PlaceCategory
import com.example.travenor.constant.TRIP_ADVISOR_API_KEY
import com.example.travenor.constant.TRIP_ADVISOR_URL
import com.example.travenor.core.ResultListener
import com.example.travenor.core.network.Callback
import com.example.travenor.core.network.NetWorker
import com.example.travenor.core.network.Response
import com.example.travenor.core.network.exception.NetworkException
import com.example.travenor.data.model.PlacePhotoResponse
import com.example.travenor.data.model.PlaceSearchResponse
import com.example.travenor.data.model.photo.PlacePhoto
import com.example.travenor.data.model.place.Place
import com.example.travenor.data.source.PlaceSource
import com.example.travenor.data.source.remote.api.PlaceApi

class PlaceRemoteDataSource private constructor(
    private val placeApi: PlaceApi
) : PlaceSource.Remote {
    override fun searchExploreAttraction(
        keyword: String,
        lat: Double,
        long: Double,
        listener: ResultListener<List<Place>>
    ) {
        val category = PlaceCategory.ATTRACTIONS.name.lowercase()

        val latLongString = "$lat,$long"

        placeApi.searchPlaceTripadvisor(
            TRIP_ADVISOR_API_KEY,
            keyword,
            category,
            latLong = latLongString
        ).enqueue(
            PlaceSearchResponse::class.java,
            object : Callback<PlaceSearchResponse> {
                override fun onResponse(
                    rawResponse: String,
                    response: Response<PlaceSearchResponse>
                ) {
                    val placeList = response.data.placeList
                    for (place in placeList) place.locationType =
                        PlaceCategory.ATTRACTIONS.name.lowercase()
                    listener.onSuccess(placeList)
                }

                override fun onFailure(t: Throwable) {
                    t.printStackTrace()
                    Log.d(LOG_TAG, t.message.toString())
                    listener.onError(NetworkException(t.message.toString()))
                }
            }
        )
    }

    override fun getNearbyPlace(
        lat: Double,
        long: Double,
        category: PlaceCategory,
        radius: Double,
        listener: ResultListener<List<Place>>
    ) {
        val latLongString = "$lat,$long"
        val categoryName = category.name.lowercase()
        placeApi.getNearbyPlace(
            TRIP_ADVISOR_API_KEY,
            latLong = latLongString,
            radius = radius,
            category = categoryName
        ).enqueue(
            PlaceSearchResponse::class.java,
            object : Callback<PlaceSearchResponse> {
                override fun onResponse(
                    rawResponse: String,
                    response: Response<PlaceSearchResponse>
                ) {
                    val placeList = response.data.placeList
                    listener.onSuccess(placeList)
                }

                override fun onFailure(t: Throwable) {
                    t.printStackTrace()
                    Log.d(LOG_TAG, t.message.toString())
                    listener.onError(NetworkException(t.message.toString()))
                }
            }
        )
    }

    override fun searchPlace(
        query: String,
        category: PlaceCategory?,
        listener: ResultListener<List<Place>>
    ) {
        val categoryName: String = category?.name?.lowercase().toString()
        placeApi.searchPlaceTripadvisor(
            TRIP_ADVISOR_API_KEY,
            query,
            categoryName
        ).enqueue(
            PlaceSearchResponse::class.java,
            object : Callback<PlaceSearchResponse> {
                override fun onResponse(rawResponse: String, response: Response<PlaceSearchResponse>) {
                    val placeList = response.data.placeList
                    for (place in placeList) place.locationType =
                        PlaceCategory.RESTAURANTS.name.lowercase()
                    listener.onSuccess(placeList)
                }

                override fun onFailure(t: Throwable) {
                    t.printStackTrace()
                    Log.d(LOG_TAG, t.message.toString())
                    listener.onError(NetworkException(t.message.toString()))
                }
            }
        )
    }

    override fun searchExploreRestaurant(
        keyword: String,
        lat: Double,
        long: Double,
        listener: ResultListener<List<Place>>
    ) {
        val category = PlaceCategory.RESTAURANTS.name.lowercase()

        val latLongString = "$lat,$long"

        placeApi.searchPlaceTripadvisor(
            TRIP_ADVISOR_API_KEY,
            keyword,
            category,
            latLong = latLongString
        ).enqueue(
            PlaceSearchResponse::class.java,
            object : Callback<PlaceSearchResponse> {
                override fun onResponse(
                    rawResponse: String,
                    response: Response<PlaceSearchResponse>
                ) {
                    val placeList = response.data.placeList
                    for (place in placeList) place.locationType =
                        PlaceCategory.RESTAURANTS.name.lowercase()
                    listener.onSuccess(placeList)
                }

                override fun onFailure(t: Throwable) {
                    t.printStackTrace()
                    Log.d(LOG_TAG, t.message.toString())
                    listener.onError(NetworkException(t.message.toString()))
                }
            }
        )
    }

    override fun searchExploreHotel(
        keyword: String,
        lat: Double,
        long: Double,
        listener: ResultListener<List<Place>>
    ) {
        val category = PlaceCategory.HOTELS.name.lowercase()
        val latLongString = "$lat,$long"

        placeApi.searchPlaceTripadvisor(
            TRIP_ADVISOR_API_KEY,
            keyword,
            category,
            latLong = latLongString
        ).enqueue(
            PlaceSearchResponse::class.java,
            object : Callback<PlaceSearchResponse> {
                override fun onResponse(
                    rawResponse: String,
                    response: Response<PlaceSearchResponse>
                ) {
                    val placeList = response.data.placeList
                    for (place in placeList) place.locationType = PlaceCategory.HOTELS.name.lowercase()
                    listener.onSuccess(placeList)
                }

                override fun onFailure(t: Throwable) {
                    t.printStackTrace()
                    Log.d(LOG_TAG, t.message.toString())
                    listener.onError(NetworkException(t.message.toString()))
                }
            }
        )
    }

    override fun getPlaceDetail(placeId: String, listener: ResultListener<Place>) {
        placeApi.getPlaceDetail(apiKey = TRIP_ADVISOR_API_KEY, locationId = placeId)
            .enqueue(
                Place::class.java,
                object : Callback<Place> {
                    override fun onResponse(rawResponse: String, response: Response<Place>) {
                        listener.onSuccess(response.data)
                    }

                    override fun onFailure(t: Throwable) {
                        t.printStackTrace()
                        listener.onError(NetworkException("Fail: ${t.message}"))
                    }
                }
            )
    }

    override fun getPlacePhoto(placeId: String, listener: ResultListener<List<PlacePhoto>>) {
        placeApi.getPlacePhoto(placeId, TRIP_ADVISOR_KEY, limit = 10)
            .enqueue(
                PlacePhotoResponse::class.java,
                object : Callback<PlacePhotoResponse> {
                    override fun onResponse(
                        rawResponse: String,
                        response: Response<PlacePhotoResponse>
                    ) {
                        Log.d(LOG_TAG, rawResponse)
                        val photoList = response.data.photoList
                        listener.onSuccess(photoList)
                    }

                    override fun onFailure(t: Throwable) {
                        t.printStackTrace()
                        Log.d(LOG_TAG, t.message.toString())
                        listener.onError(NetworkException(t.message.toString()))
                    }
                }
            )
    }

    companion object {
        private var instance: PlaceRemoteDataSource? = null
        private const val CONNECT_TIME_OUT = 5000
        private const val READ_TIME_OUT = 5000
        const val LOG_TAG = "PLACE_RSOURCE"

        @RequiresApi(Build.VERSION_CODES.O)
        fun getInstance() = synchronized(this) {
            val placeApi = NetWorker.getInstance(
                TRIP_ADVISOR_URL,
                CONNECT_TIME_OUT,
                READ_TIME_OUT
            ).create(PlaceApi::class.java)
            instance ?: PlaceRemoteDataSource(placeApi).also { instance = it }
        }
    }
}
