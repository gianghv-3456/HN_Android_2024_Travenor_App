package com.example.travenor.data.place.source

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
import com.example.travenor.data.model.photo.PlacePhoto
import com.example.travenor.data.model.place.Place
import com.example.travenor.data.place.PlacePhotoResponse
import com.example.travenor.data.place.PlaceSearchResponse
import com.example.travenor.data.place.api.PlaceApi

class PlaceRemoteDataSource private constructor(
    private val placeApi: PlaceApi
) : PlaceSource.Remote {
    override fun searchExploreAttraction(keyword: String, listener: ResultListener<List<Place>>) {
        val category = PlaceCategory.ATTRACTION.name.lowercase()

        placeApi.searchPlaceTripadvisor(
            TRIP_ADVISOR_API_KEY,
            keyword,
            category
        ).enqueue(
            PlaceSearchResponse::class.java,
            object : Callback<PlaceSearchResponse> {
                override fun onResponse(
                    rawResponse: String,
                    response: Response<PlaceSearchResponse>
                ) {
                    Log.d(LOG_TAG, rawResponse)
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

    override fun searchExploreRestaurant(keyword: String, listener: ResultListener<List<Place>>) {
        val category = PlaceCategory.RESTAURANT.name.lowercase()

        placeApi.searchPlaceTripadvisor(
            TRIP_ADVISOR_API_KEY,
            keyword,
            category
        ).enqueue(
            PlaceSearchResponse::class.java,
            object : Callback<PlaceSearchResponse> {
                override fun onResponse(
                    rawResponse: String,
                    response: Response<PlaceSearchResponse>
                ) {
                    Log.d(LOG_TAG, rawResponse)
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

    override fun searchExploreHotel(keyword: String, listener: ResultListener<List<Place>>) {
        val category = PlaceCategory.HOTEL.name.lowercase()

        placeApi.searchPlaceTripadvisor(
            TRIP_ADVISOR_API_KEY,
            keyword,
            category
        ).enqueue(
            PlaceSearchResponse::class.java,
            object : Callback<PlaceSearchResponse> {
                override fun onResponse(
                    rawResponse: String,
                    response: Response<PlaceSearchResponse>
                ) {
                    Log.d(LOG_TAG, rawResponse)
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

    override fun getPlacePhoto(placeId: String, listener: ResultListener<List<PlacePhoto>>) {
        placeApi.getPlacePhoto(placeId, TRIP_ADVISOR_KEY)
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
            val placeApi = NetWorker(
                TRIP_ADVISOR_URL,
                CONNECT_TIME_OUT,
                READ_TIME_OUT
            ).create(PlaceApi::class.java)
            instance ?: PlaceRemoteDataSource(placeApi).also { instance = it }
        }
    }
}
