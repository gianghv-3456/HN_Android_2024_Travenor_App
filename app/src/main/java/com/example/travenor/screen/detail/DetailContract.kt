package com.example.travenor.screen.detail

import com.example.travenor.data.model.photo.PlacePhoto
import com.example.travenor.data.model.place.Place
import com.example.travenor.utils.base.BasePresenter

interface DetailContract {
    interface View {
        fun onGetPlaceDetailSuccess(place: Place)
        fun onGetPlaceDetailFail(e: Exception)
        fun onGetPhotoSuccess(photos: PlacePhoto)
    }

    interface Presenter : BasePresenter<View> {
        fun getPlaceDetail(locationId: String)

        fun getPlacePhotos(locationId: String)
    }
}
