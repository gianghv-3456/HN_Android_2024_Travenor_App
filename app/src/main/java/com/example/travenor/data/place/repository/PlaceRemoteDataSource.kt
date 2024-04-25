package com.example.travenor.data.place.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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
import com.example.travenor.data.place.source.PlaceSource

class PlaceRemoteDataSource private constructor(
    private val placeApi: PlaceApi
) : PlaceSource.Remote {
    override fun searchExploreAttraction(listener: ResultListener<List<Place>>) {
        // TODO implement get user interest value
        placeApi.searchPlaceTripadvisor(
            "C95F15F125404681AF85A93A455358C7",
            "MOUNTAIN",
            "attractions"
        ).enqueue(
            PlaceSearchResponse::class.java,
            object : Callback<PlaceSearchResponse> {
                override fun onResponse(
                    rawResponse: String,
                    response: Response<PlaceSearchResponse>
                ) {
                    Log.d("R_SOURCE_PLACE", rawResponse)
                    val placeList = response.data.placeList
                    listener.onSuccess(placeList)
                }

                override fun onFailure(t: Throwable) {
                    t.printStackTrace()
                    Log.d("R_SOURCE_PLACE", t.message.toString())
                    listener.onError(NetworkException(t.message.toString()))
                }
            }
        )
    }

    override fun searchExploreRestaurant(listener: ResultListener<List<Place>>) {
        // TODO implement get user interest value
        placeApi.searchPlaceTripadvisor(
            "C95F15F125404681AF85A93A455358C7",
            "Sea Food",
            "restaurant"
        ).enqueue(
            PlaceSearchResponse::class.java,
            object : Callback<PlaceSearchResponse> {
                override fun onResponse(
                    rawResponse: String,
                    response: Response<PlaceSearchResponse>
                ) {
                    Log.d("R_SOURCE_PLACE", rawResponse)
                    val placeList = response.data.placeList
                    listener.onSuccess(placeList)
                }

                override fun onFailure(t: Throwable) {
                    t.printStackTrace()
                    Log.d("R_SOURCE_PLACE", t.message.toString())
                    listener.onError(NetworkException(t.message.toString()))
                }
            }
        )
    }

    override fun getPlacePhoto(placeId: String, listener: ResultListener<List<PlacePhoto>>) {
        placeApi.getPlacePhoto(placeId, "C95F15F125404681AF85A93A455358C7")
            .enqueue(
                PlacePhotoResponse::class.java,
                object : Callback<PlacePhotoResponse> {
                    override fun onResponse(
                        rawResponse: String,
                        response: Response<PlacePhotoResponse>
                    ) {
                        Log.d("R_SOURCE_PLACE", rawResponse)
                        val photoList = response.data.photoList
                        listener.onSuccess(photoList)
                    }

                    override fun onFailure(t: Throwable) {
                        t.printStackTrace()
                        Log.d("R_SOURCE_PLACE", t.message.toString())
                        listener.onError(NetworkException(t.message.toString()))
                    }
                }
            )
    }

    override fun searchExploreHotel(listener: ResultListener<List<Place>>) {
        placeApi.searchPlaceTripadvisor(
            "C95F15F125404681AF85A93A455358C7",
            "hotel",
            "hotel"
        ).enqueue(
            PlaceSearchResponse::class.java,
            object : Callback<PlaceSearchResponse> {
                override fun onResponse(
                    rawResponse: String,
                    response: Response<PlaceSearchResponse>
                ) {
                    Log.d("R_SOURCE_PLACE", rawResponse)
                    val placeList = response.data.placeList
                    listener.onSuccess(placeList)
                }

                override fun onFailure(t: Throwable) {
                    t.printStackTrace()
                    Log.d("R_SOURCE_PLACE", t.message.toString())
                    listener.onError(NetworkException(t.message.toString()))
                }
            }
        )
    }

    companion object {
        private var instance: PlaceRemoteDataSource? = null
        private const val CONNECT_TIME_OUT = 5000
        private const val READ_TIME_OUT = 5000

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
