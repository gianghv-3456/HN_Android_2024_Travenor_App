package com.example.travenor.data.model.photo

import com.google.gson.annotations.SerializedName

data class ImageList(
    @SerializedName("thumbnail") val thumbnail: Image?,
    @SerializedName("small") val small: Image?,
    @SerializedName("medium") val medium: Image?,
    @SerializedName("large") val large: Image?,
    @SerializedName("original") val original: Image?
) {
    fun getBiggestImageAvailable(): Image? {
        return original ?: large ?: medium ?: small ?: thumbnail
    }
}
