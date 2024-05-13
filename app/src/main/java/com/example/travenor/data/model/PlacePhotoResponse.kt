package com.example.travenor.data.model

import com.example.travenor.data.model.photo.PlacePhoto
import com.google.gson.annotations.SerializedName

data class PlacePhotoResponse(
    @SerializedName("data") val photoList: List<PlacePhoto>
)
