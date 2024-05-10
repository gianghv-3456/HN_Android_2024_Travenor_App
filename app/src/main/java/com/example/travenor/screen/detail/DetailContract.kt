package com.example.travenor.screen.detail

import com.example.travenor.data.model.photo.PlacePhoto
import com.example.travenor.data.model.place.Place
import com.example.travenor.utils.base.BasePresenter

interface DetailContract {
    interface View {
        fun onGetPlaceDetailSuccess(place: Place)
        fun onGetPlaceDetailFail(e: Exception)
        fun onGetPhotoSuccess(photos: PlacePhoto)
        fun onMarkFavoriteSuccess()
        fun onMarkNotFavoriteSuccess()
    }

    interface Presenter : BasePresenter<View> {
        fun getPlaceDetail(locationId: String)

        fun getPlacePhotos(locationId: String)

        fun markFavorite(placeId: String)
        fun markNotFavorite(placeId: String)
    }
}
