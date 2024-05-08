package com.example.travenor.screen.moredetail

import com.example.travenor.core.ResultListener
import com.example.travenor.data.model.photo.PlacePhoto
import com.example.travenor.data.model.place.Place
import com.example.travenor.data.place.repository.PlaceRepository

class MoreDetailPresenter internal constructor(private val placeRepositoryImpl: PlaceRepository) :
    MoreDetailContract.Presenter {
    private var mView: MoreDetailContract.View? = null
    override fun getPlaceDetail(locationId: String) {
        placeRepositoryImpl.getPlaceDetail(
            locationId,
            object : ResultListener<Place> {
                override fun onSuccess(data: Place?) {
                    if (data != null) {
                        mView?.onGetPlaceDetailSuccess(data)
                    } else {
                        mView?.onGetPlaceDetailFail(Exception("Data null when get place detail"))
                    }
                }

                override fun onError(exception: Exception?) {
                    exception?.printStackTrace()
                    mView?.onGetPlaceDetailFail(exception!!)
                }
            }
        )
    }

    override fun getPlacePhotos(locationId: String) {
        placeRepositoryImpl.getPlacePhoto(
            locationId,
            object : ResultListener<List<PlacePhoto>> {
                override fun onSuccess(data: List<PlacePhoto>?) {
                    if (data.isNullOrEmpty()) return

                    for (photo in data)
                        photo.locationId = locationId

                    mView?.onGetPhotoSuccess(data)
                }

                override fun onError(exception: Exception?) { /* no-op */
                }
            }
        )
    }

    override fun onStart() {
        /* no-op */
    }

    override fun onStop() {
        /* no-op */
    }

    override fun setView(view: MoreDetailContract.View?) {
        mView = view
    }
}
