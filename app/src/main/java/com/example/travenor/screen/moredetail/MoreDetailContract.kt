package com.example.travenor.screen.moredetail

import com.example.travenor.data.model.photo.PlacePhoto
import com.example.travenor.data.model.place.Place
import com.example.travenor.utils.base.BasePresenter

interface MoreDetailContract {
    interface View {
        fun onGetPlaceDetailSuccess(place: Place)
        fun onGetPlaceDetailFail(e: Exception)
        fun onGetPhotoSuccess(photos: List<PlacePhoto>)
    }

    interface Presenter : BasePresenter<View> {
        fun getPlaceDetail(locationId: String)

        fun getPlacePhotos(locationId: String)
    }
}
