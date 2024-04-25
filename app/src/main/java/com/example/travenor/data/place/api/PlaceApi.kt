package com.example.travenor.data.place.api

import com.example.travenor.constant.DEFAULT_LAT
import com.example.travenor.constant.DEFAULT_LONG
import com.example.travenor.core.network.Call
import com.example.travenor.core.network.annotation.GET
import com.example.travenor.core.network.annotation.Path
import com.example.travenor.core.network.annotation.Query
import com.example.travenor.data.place.PlacePhotoResponse
import com.example.travenor.data.place.PlaceSearchResponse

interface PlaceApi {
    @GET("/location/search")
    fun searchPlaceTripadvisor(
        @Query("key") apiKey: String,
        @Query("searchQuery") searchQuery: String,
        @Query("category") category: String = "attraction",
        @Query("latLong") latLong: String = "$DEFAULT_LAT,$DEFAULT_LONG",
        @Query("language") language: String = "vi"
    ): Call<PlaceSearchResponse>

    @GET("/location/{locationId}/photos")
    fun getPlacePhoto(
        @Path("locationId") locationId: String,
        @Query("key") apiKey: String,
        @Query("limit") limit: Int = 1,
        @Query("language") language: String = "vi"
    ): Call<PlacePhotoResponse>
}
