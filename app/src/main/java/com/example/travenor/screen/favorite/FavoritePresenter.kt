package com.example.travenor.screen.favorite

import com.example.travenor.core.ResultListener
import com.example.travenor.data.model.photo.PlacePhoto
import com.example.travenor.data.model.place.Place
import com.example.travenor.data.place.repository.PlaceRepository

class FavoritePresenter(
    private val placeRepository: PlaceRepository
) : FavoriteContract.Presenter {
    private var view: FavoriteContract.View? = null
    override fun getFavoritePlaces() {
        placeRepository.getFavoritePlace(object : ResultListener<List<Place>> {
            override fun onSuccess(data: List<Place>?) {
                if (data == null) {
                    view?.onGetFavoritePlacesFailed("No Data")
                } else {
                    view?.onGetFavoritePlacesSuccess(data)
                }
            }

            override fun onError(exception: Exception?) {
                view?.onGetFavoritePlacesFailed(exception?.message.toString())
            }
        })
    }

    override fun getPlacePhoto(placeId: String) {
        placeRepository.getPlacePhoto(
            placeId,
            object : ResultListener<List<PlacePhoto>> {
                override fun onSuccess(data: List<PlacePhoto>?) {
                    if (data.isNullOrEmpty()) return
                    for (photo in data) photo.locationId = placeId

                    view?.onGetPlacePhotoSuccess(placeId, data)
                }

                override fun onError(exception: Exception?) { /* no-op */
                }
            }
        )
    }

    override fun removeFavoritePlace(placeId: String) {
        placeRepository.unmarkFavorite(
            placeId,
            object : ResultListener<Boolean> {
                override fun onSuccess(data: Boolean?) {
                    if (data == null) {
                        return
                    } else {
                        if (data) view?.onRemoveFavoritePlaceSuccess(placeId)
                    }
                }

                override fun onError(exception: Exception?) {
                    exception?.printStackTrace()
                }
            }
        )
    }

    override fun onStart() { /* no-op */
    }

    override fun onStop() { /* no-op */
    }

    override fun setView(view: FavoriteContract.View?) {
        this.view = view
    }
}
