package com.example.travenor.screen.favorite

import com.example.travenor.data.model.photo.PlacePhoto
import com.example.travenor.data.model.place.Place
import com.example.travenor.utils.base.BasePresenter

interface FavoriteContract {
    interface View {
        fun onGetFavoritePlacesSuccess(places: List<Place>)
        fun onGetFavoritePlacesFailed(message: String)
        fun onGetPlacePhotoSuccess(placeId: String, photos: List<PlacePhoto>)
        fun onRemoveFavoritePlaceSuccess(placeId: String)
    }

    interface Presenter : BasePresenter<View> {
        fun getFavoritePlaces()

        fun getPlacePhoto(placeId: String)

        fun removeFavoritePlace(placeId: String)
    }
}
