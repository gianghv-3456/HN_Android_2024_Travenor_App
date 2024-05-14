package com.example.travenor.screen.search

import android.os.Handler
import android.os.Looper
import com.example.travenor.constant.PlaceCategory
import com.example.travenor.core.ResultListener
import com.example.travenor.data.model.photo.PlacePhoto
import com.example.travenor.data.model.place.Place
import com.example.travenor.data.repository.PlaceRepository
import java.util.concurrent.Callable
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors

class SearchPresenter internal constructor(
    private val placeRepositoryImpl: PlaceRepository
) : SearchContract.Presenter {
    private var mView: SearchContract.View? = null

    override fun getRecentSearchString() {
        placeRepositoryImpl.getRecentSearch(object : ResultListener<List<String>> {
            override fun onSuccess(data: List<String>?) {
                data?.let {
                    mView?.onGetRecentSearchStringSuccess(it)
                }
            }

            override fun onError(exception: Exception?) { /* no-op */
            }
        })
    }

    override fun onSearchPlace(searchString: String, category: PlaceCategory?) {
        placeRepositoryImpl.searchPlace(
            searchString,
            category,
            object : ResultListener<List<Place>> {
                override fun onSuccess(data: List<Place>?) {
                    if (data.isNullOrEmpty()) {
                        mView?.onSearchPlaceFail("No data!")
                    } else {
                        val locationIdList = mutableListOf<String>()
                        data.forEach {
                            locationIdList.add(it.locationId)
                        }

                        fetchPlaceDetail(data.map { it.locationId }.toMutableList())
                    }
                }

                override fun onError(exception: Exception?) {
                    exception?.printStackTrace()
                    mView?.onSearchPlaceFail(exception?.message.toString())
                }
            }
        )
    }

    override fun getPlaceThumbnailPhoto(placeId: String) {
        placeRepositoryImpl.getPlacePhoto(
            placeId,
            object : ResultListener<List<PlacePhoto>> {
                override fun onSuccess(data: List<PlacePhoto>?) {
                    if (data.isNullOrEmpty()) return

                    mView?.onGetPlacePhotoSuccess(placeId, data)
                }

                override fun onError(exception: Exception?) { /* no-op */
                }
            }
        )
    }

    override fun saveRecentSearchString(searchString: List<String>) {
        placeRepositoryImpl.saveRecentSearch(searchString)
    }

    private fun fetchPlaceDetail(placeIdList: MutableList<String>) {
        val executorService = Executors.newFixedThreadPool(MAX_THREAD_POOL_SIZE)
        val taskList = mutableListOf<Callable<Place>>()

        placeIdList.forEach {
            // Callable task to get detail for each placeId
            val callable = Callable {
                val future = CompletableFuture<Place>()

                // Get detail
                placeRepositoryImpl.getPlaceDetail(
                    it,
                    object : ResultListener<Place> {
                        override fun onSuccess(data: Place?) {
                            future.complete(data)
                        }

                        override fun onError(exception: Exception?) {
                            future.complete(null)
                        }
                    }
                )
                return@Callable future.get()
            }
            taskList.add(callable)
        }

        executorService.execute {
            val futureList = executorService.invokeAll(taskList)

            val result = mutableListOf<Place>()
            futureList.forEach {
                it.get()?.let { it1 -> result.add(it1) }
            }

            // post to main ui thread
            Handler(Looper.getMainLooper()).post {
                mView?.onSearchPlaceSuccess(result)
            }
        }
    }

    override fun onStart() { /* no-op */
    }

    override fun onStop() { /* no-op */
    }

    override fun setView(view: SearchContract.View?) {
        mView = view
    }

    companion object {
        private const val MAX_THREAD_POOL_SIZE = 5
    }
}
