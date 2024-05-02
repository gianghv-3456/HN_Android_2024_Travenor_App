package com.example.travenor.data.model.photo

import com.google.gson.annotations.SerializedName

data class PlacePhoto(
    @SerializedName("id") val id: String,
    @SerializedName("location_id") var locationId: String,
    @SerializedName("caption") val caption: String,
    @SerializedName("images") val imageList: ImageList
)
