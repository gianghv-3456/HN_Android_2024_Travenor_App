package com.example.travenor.screen.detail

import com.example.travenor.core.ResultListener
import com.example.travenor.data.model.photo.PlacePhoto
import com.example.travenor.data.model.place.Place
import com.example.travenor.data.repository.PlaceRepository

class DetailPresenter internal constructor(private val placeRepositoryImpl: PlaceRepository) :
    DetailContract.Presenter {
    private var mView: DetailContract.View? = null
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
                    val photo = data.first()
                    photo.locationId = locationId

                    mView?.onGetPhotoSuccess(photo)
                }

                override fun onError(exception: Exception?) { /* no-op */
                }
            }
        )
    }

    override fun markFavorite(placeId: String) {
        placeRepositoryImpl.markFavorite(
            placeId,
            object : ResultListener<Boolean> {
                override fun onSuccess(data: Boolean?) {
                    if (data == true) mView?.onMarkFavoriteSuccess()
                }

                override fun onError(exception: Exception?) {
                    exception?.printStackTrace()
                    /* no-op */
                }
            }
        )
    }

    override fun markNotFavorite(placeId: String) {
        placeRepositoryImpl.markNotFavorite(
            placeId,
            object : ResultListener<Boolean> {
                override fun onSuccess(data: Boolean?) {
                    if (data == true) mView?.onMarkNotFavoriteSuccess()
                }

                override fun onError(exception: Exception?) {
                    exception?.printStackTrace()
                    /* no-op */
                }
            }
        )
    }

    override fun setView(view: DetailContract.View?) {
        this.mView = view
    }
}
