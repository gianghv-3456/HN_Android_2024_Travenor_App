package com.example.travenor.screen.home

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.ViewCompat
import com.example.travenor.constant.PlaceCategory
import com.example.travenor.data.model.photo.PlacePhoto
import com.example.travenor.data.model.place.Place
import com.example.travenor.data.place.repository.PlaceRemoteDataSource
import com.example.travenor.data.place.repository.PlaceRepository
import com.example.travenor.databinding.FragmentHomeBinding
import com.example.travenor.screen.MainActivity
import com.example.travenor.screen.home.apdater.PlaceListAdapter
import com.example.travenor.utils.base.BaseFragment

class HomeFragment : BaseFragment<FragmentHomeBinding>(), ExplorePlaceContract.View {

    private lateinit var mExplorePlacePresenter: ExplorePlacePresenter
    private val mAttractionAdapter: PlaceListAdapter by lazy { PlaceListAdapter() }
    private val mRestaurantAdapter: PlaceListAdapter by lazy { PlaceListAdapter() }
    private val mHotelAdapter: PlaceListAdapter by lazy { PlaceListAdapter() }

    override fun inflateViewBinding(inflater: LayoutInflater): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun initData() {
        val placeRepository = PlaceRepository.getInstance(PlaceRemoteDataSource.getInstance())
        mExplorePlacePresenter = ExplorePlacePresenter(placeRepository)
        mExplorePlacePresenter.setView(this)
        mExplorePlacePresenter.getExploreAttraction()
        mExplorePlacePresenter.getExploreRestaurant()
        mExplorePlacePresenter.getExploreHotel()
    }

    override fun initView() {
        setLayoutBelowSystemBar()
        viewBinding.listAttractionRecycler.adapter = mAttractionAdapter
        viewBinding.listRestaurantRecycler.adapter = mRestaurantAdapter
        viewBinding.listHotelRecycler.adapter = mHotelAdapter

        (activity as MainActivity).setWhiteStatusBar()
    }

    private fun setLayoutBelowSystemBar() {
        ViewCompat.setOnApplyWindowInsetsListener(viewBinding.layoutAppBar) { v, insets ->
            val layoutParams = v.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.topMargin = insets.systemWindowInsetTop
            v.layoutParams = layoutParams
            insets
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }

    override fun onGetExplorePlaceSuccess(locationList: List<Place>, placeCategory: PlaceCategory) {
        activity?.runOnUiThread {
            when (placeCategory) {
                PlaceCategory.ATTRACTION -> {
                    viewBinding.containerExploreAttractionTitle.visibility = View.VISIBLE
                    mAttractionAdapter.setData(locationList)
                }

                PlaceCategory.RESTAURANT -> {
                    viewBinding.containerExploreRestaurantTitle.visibility = View.VISIBLE
                    mRestaurantAdapter.setData(locationList)
                }

                PlaceCategory.HOTEL -> {
                    viewBinding.containerExploreHotelTitle.visibility = View.VISIBLE
                    mHotelAdapter.setData(locationList)
                }
            }
        }
    }

    override fun onGetExplorePlaceFail(exception: Exception?, placeCategory: PlaceCategory) {
        activity?.runOnUiThread {
            when (placeCategory) {
                PlaceCategory.ATTRACTION ->
                    viewBinding.containerExploreAttractionTitle.visibility =
                        View.GONE

                PlaceCategory.RESTAURANT ->
                    viewBinding.containerExploreRestaurantTitle.visibility =
                        View.GONE

                PlaceCategory.HOTEL -> viewBinding.containerExploreHotelTitle.visibility = View.GONE
            }
        }
    }

    override fun onGetPhotoSuccess(photos: PlacePhoto, placeCategory: PlaceCategory) {
        activity?.runOnUiThread {
            val locationId = photos.locationId
            mAttractionAdapter.updateThumbnail(locationId, photos)

            when (placeCategory) {
                PlaceCategory.ATTRACTION -> mAttractionAdapter.updateThumbnail(locationId, photos)
                PlaceCategory.RESTAURANT -> mRestaurantAdapter.updateThumbnail(locationId, photos)
                PlaceCategory.HOTEL -> mHotelAdapter.updateThumbnail(locationId, photos)
            }
        }
    }
}
