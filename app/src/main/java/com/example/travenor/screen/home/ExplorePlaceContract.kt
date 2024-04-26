package com.example.travenor.screen.home

import com.example.travenor.constant.PlaceCategory
import com.example.travenor.data.model.photo.PlacePhoto
import com.example.travenor.data.model.place.Place
import com.example.travenor.utils.base.BasePresenter

class ExplorePlaceContract {
    interface View {
        fun onGetExplorePlaceSuccess(locationList: List<Place>, placeCategory: PlaceCategory)
        fun onGetExplorePlaceFail(exception: Exception?, placeCategory: PlaceCategory)
        fun onGetPhotoSuccess(photos: PlacePhoto, placeCategory: PlaceCategory)
        fun onGetUserInterestPlaceDone()
        fun onGetUserInterestFoodDone()
    }

    interface Presenter : BasePresenter<View> {
        fun getExploreAttraction()
        fun getExploreRestaurant()
        fun getExploreHotel()
        fun getUserInterest()
    }
}
