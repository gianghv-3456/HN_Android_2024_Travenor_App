package com.example.travenor.data.place

import com.example.travenor.data.model.place.Place
import com.google.gson.annotations.SerializedName

data class PlaceSearchResponse(
    @SerializedName("data") val placeList: List<Place>
)
