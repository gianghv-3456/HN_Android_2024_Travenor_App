package com.example.travenor.data.model.place

import com.google.gson.annotations.SerializedName

data class Place(
    @SerializedName("location_id") val locationId: String,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("web_url") val webUrl: String,
    @SerializedName("address_obj") val addressObj: Address,
    @SerializedName("latitude") val latitude: Float,
    @SerializedName("longitude") val longitude: Float,
    @SerializedName("rating") val rating: Float,
    @SerializedName("num_reviews") val ratingAmount: Int,
    @SerializedName("photo_count") val photoAmount: Int,
    @SerializedName("see_all_photos") val seeAllPhotosUrl: String,
    @SerializedName("place_category") val locationType: String
)
