package com.example.travenor.data.model.review

import com.google.gson.annotations.SerializedName

data class Review(
    @SerializedName("id") val id: String,
    @SerializedName("location_id") val locationId: String,
    @SerializedName("rating") val rating: Float,
    @SerializedName("text") val text: String,
    @SerializedName("title") val title: String,
    @SerializedName("published_date") val date: String
)
