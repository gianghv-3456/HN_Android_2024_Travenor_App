package com.example.travenor.screen.moredetail

import com.example.travenor.constant.PlaceCategory
import com.example.travenor.data.model.photo.PlacePhoto
import com.example.travenor.data.model.place.Place
import com.example.travenor.utils.base.BasePresenter

interface MoreDetailContract {
    interface View {
        fun onGetPlaceDetailSuccess(place: Place)
        fun onGetPlaceDetailFail(e: Exception)
        fun onGetPhotoSuccess(photos: List<PlacePhoto>)

        fun onGetNearbyRestaurantSuccess(place: List<Place>)
        fun onGetNearbyHotelSuccess(places: List<Place>)

        fun onGetNearbyPlacePhotoSuccess(photos: List<PlacePhoto>, category: PlaceCategory)
        fun onMarkFavoriteSuccess()
        fun onMarkNotFavoriteSuccess()
    }

    interface Presenter : BasePresenter<View> {
        fun getPlaceDetail(locationId: String)

        fun getPlacePhotos(locationId: String)

        fun getNearbyRestaurant(lat: Double, long: Double)

        fun getNearbyHotel(lat: Double, long: Double)

        fun getNearbyPlacePhoto(locationId: String, category: PlaceCategory)
        fun markFavorite(placeId: String, isFavorite: Boolean)
    }
}
