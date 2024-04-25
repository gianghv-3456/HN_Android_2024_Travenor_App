package com.example.travenor.screen.home

import com.example.travenor.constant.PlaceCategory
import com.example.travenor.core.ResultListener
import com.example.travenor.data.model.photo.PlacePhoto
import com.example.travenor.data.model.place.Place
import com.example.travenor.data.place.repository.PlaceRepository

class ExplorePlacePresenter internal constructor(
    private val placeRepository: PlaceRepository
) : ExplorePlaceContract.Presenter {
    private var mView: ExplorePlaceContract.View? = null
    override fun getExploreAttraction() {
        placeRepository.searchExploreAttraction(object : ResultListener<List<Place>> {
            override fun onSuccess(data: List<Place>?) {
                if (data.isNullOrEmpty()) {
                    mView?.onGetExplorePlaceFail(
                        Exception("Get null"),
                        PlaceCategory.ATTRACTION
                    )
                }

                val locationIdList = mutableListOf<String>()
                data!!.forEach {
                    locationIdList.add(it.locationId)
                }
                mView?.onGetExplorePlaceSuccess(data, PlaceCategory.ATTRACTION)
                getThumbnail(locationIdList, PlaceCategory.ATTRACTION)
            }

            override fun onError(exception: Exception?) {
                mView?.onGetExplorePlaceFail(exception, PlaceCategory.ATTRACTION)
            }
        })
    }

    private fun getThumbnail(placeIdList: MutableList<String>, category: PlaceCategory) {
        placeIdList.forEach {
            placeRepository.getPlacePhoto(
                it,
                object : ResultListener<List<PlacePhoto>> {
                    override fun onSuccess(data: List<PlacePhoto>?) {
                        if (data.isNullOrEmpty()) return
                        val photo = data.first()
                        photo.locationId = it

                        mView?.onGetPhotoSuccess(photo, category)
                    }

                    override fun onError(exception: Exception?) {
                        //
                    }
                }
            )
        }
    }

    override fun getExploreRestaurant() {
        placeRepository.searchExploreRestaurant(object : ResultListener<List<Place>> {

            override fun onSuccess(data: List<Place>?) {
                if (data.isNullOrEmpty()) {
                    mView?.onGetExplorePlaceFail(
                        Exception("get null"),
                        PlaceCategory.RESTAURANT
                    )
                }
                val locationIdList = mutableListOf<String>()
                data!!.forEach {
                    locationIdList.add(it.locationId)
                }
                mView?.onGetExplorePlaceSuccess(data, PlaceCategory.RESTAURANT)
                getThumbnail(locationIdList, PlaceCategory.RESTAURANT)
            }

            override fun onError(exception: Exception?) {
                mView?.onGetExplorePlaceFail(exception, PlaceCategory.RESTAURANT)
            }
        })
    }

    override fun getExploreHotel() {
        placeRepository.searchExploreHotel(object : ResultListener<List<Place>> {
            override fun onSuccess(data: List<Place>?) {
                if (data.isNullOrEmpty()) {
                    mView?.onGetExplorePlaceFail(
                        Exception("get null"),
                        PlaceCategory.HOTEL
                    )
                }
                val locationIdList = mutableListOf<String>()
                data!!.forEach {
                    locationIdList.add(it.locationId)
                }
                mView?.onGetExplorePlaceSuccess(data, PlaceCategory.HOTEL)
                getThumbnail(locationIdList, PlaceCategory.HOTEL)
            }

            override fun onError(exception: Exception?) {
                mView?.onGetExplorePlaceFail(exception, PlaceCategory.HOTEL)
            }
        })
    }

    override fun onStart() {
//        TODO("Not yet implemented")
    }

    override fun onStop() {
//        TODO("Not yet implemented")
    }

    override fun setView(view: ExplorePlaceContract.View?) {
        this.mView = view
    }
}
