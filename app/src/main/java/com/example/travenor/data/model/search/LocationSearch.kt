package com.example.travenor.data.model.search

import com.example.travenor.data.model.place.Address
import com.google.gson.annotations.SerializedName

data class LocationSearch(
    @SerializedName("name") val name: String,
    @SerializedName("location_id") val locationId: String,
    @SerializedName("distance") val distance: String,
    @SerializedName("address_obj") val addressObj: Address
)
