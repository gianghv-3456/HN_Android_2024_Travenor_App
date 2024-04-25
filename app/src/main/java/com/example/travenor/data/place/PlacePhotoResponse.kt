package com.example.travenor.data.place

import com.example.travenor.data.model.photo.PlacePhoto
import com.google.gson.annotations.SerializedName

data class PlacePhotoResponse(
    @SerializedName("data") val photoList: List<PlacePhoto>
)
