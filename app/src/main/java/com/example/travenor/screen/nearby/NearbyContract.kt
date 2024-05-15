package com.example.travenor.screen.nearby

import com.example.travenor.data.model.photo.PlacePhoto
import com.example.travenor.data.model.place.Place
import com.example.travenor.utils.base.BasePresenter

interface NearbyContract {
    interface View {
        fun onGetNearbyAttractionSuccess(placePhotos: List<PlacePhoto>, places: List<Place>)
        fun onGetNearbyHotelSuccess(placePhotos: List<PlacePhoto>, places: List<Place>)
        fun onGetNearbyRestaurantSuccess(placePhotos: List<PlacePhoto>, places: List<Place>)
    }

    interface Presenter : BasePresenter<View> {
        fun getNearbyRestaurant(lat: Double, long: Double)
        fun getNearbyHotel(lat: Double, long: Double)
        fun getNearbyAttraction(lat: Double, long: Double)
    }
}
