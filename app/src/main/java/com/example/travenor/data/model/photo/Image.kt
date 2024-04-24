package com.example.travenor.data.model.photo

import com.google.gson.annotations.SerializedName

data class Image(
    @SerializedName("height") val height: Int,
    @SerializedName("width") val width: Int,
    @SerializedName("url") val url: String

)
