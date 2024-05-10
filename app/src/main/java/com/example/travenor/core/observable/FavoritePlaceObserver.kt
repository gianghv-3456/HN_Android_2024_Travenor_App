package com.example.travenor.core.observable

interface FavoritePlaceObserver {
    fun onFavoritePlaceChange(placeId: String, isFavorite: Int)
}
