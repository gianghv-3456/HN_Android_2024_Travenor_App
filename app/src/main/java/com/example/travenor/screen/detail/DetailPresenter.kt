package com.example.travenor.screen.detail

import com.example.travenor.core.ResultListener
import com.example.travenor.data.model.photo.PlacePhoto
import com.example.travenor.data.model.place.Place
import com.example.travenor.data.place.repository.PlaceRepository

class DetailPresenter internal constructor(private val placeRepository: PlaceRepository) :
    DetailContract.Presenter {
    private var mView: DetailContract.View? = null
    override fun getPlaceDetail(locationId: String) {
        placeRepository.getPlaceDetail(
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
        placeRepository.getPlacePhoto(
            locationId,
            object : ResultListener<List<PlacePhoto>> {
                override fun onSuccess(data: List<PlacePhoto>?) {
                    if (data.isNullOrEmpty()) return
                    val photo = data.first()
                    photo.locationId = locationId

                    mView?.onGetPhotoSuccess(photo)
                }

                override fun onError(exception: Exception?) { /* no-op */
                }
            }
        )
    }

    override fun onStart() { /* no-op */
    }

    override fun onStop() { /* no-op */
    }

    override fun setView(view: DetailContract.View?) {
        this.mView = view
    }
}
