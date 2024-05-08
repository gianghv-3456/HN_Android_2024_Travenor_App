package com.example.travenor.data.model.place

import com.google.gson.annotations.SerializedName

data class Address(
    @SerializedName("street1") val street1: String?,
    @SerializedName("city") val city: String?,
    @SerializedName("state") val state: String?,
    @SerializedName("country") val country: String?,
    @SerializedName("postalcode") val postalCode: String?,
    @SerializedName("address_string") val addressString: String?
) {
    fun getAddress(): String {
        val stringBuilder = StringBuilder()
        if (street1?.isNotEmpty() == true) {
            stringBuilder.append(street1)
            stringBuilder.append(", ")
        }
        if (city?.isNotEmpty() == true) {
            stringBuilder.append(city)
            stringBuilder.append(", ")
        }
        if (state?.isNotEmpty() == true) {
            stringBuilder.append(state)
            stringBuilder.append(", ")
        }
        if (country?.isNotEmpty() == true) {
            stringBuilder.append(country)
        }
        return stringBuilder.toString()
    }
}
