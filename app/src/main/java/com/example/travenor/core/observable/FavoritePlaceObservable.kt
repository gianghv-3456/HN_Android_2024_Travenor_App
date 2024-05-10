package com.example.travenor.core.observable

interface FavoritePlaceObservable {
    // register the observer with this method
    fun registerObserver(favoritePlaceObserver: FavoritePlaceObserver?)

    // unregister the observer with this method
    fun removeObserver(favoritePlaceObserver: FavoritePlaceObserver?)

    // call this method upon database change
    fun notifyChanged(placeId: String, isFavorite: Int)
}
