package com.example.travenor.data.model.review

import com.google.gson.annotations.SerializedName

data class PlaceTip(
    @SerializedName("id") val id: String,
    @SerializedName("text") val text: String,
    @SerializedName("agree_count") val agreeCount: Int,
    @SerializedName("disagree_count") val disagreeCount: Int
)
