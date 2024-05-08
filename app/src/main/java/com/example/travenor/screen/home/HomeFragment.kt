package com.example.travenor.screen.home

import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.ViewCompat
import com.example.travenor.R
import com.example.travenor.constant.PlaceCategory
import com.example.travenor.data.model.photo.PlacePhoto
import com.example.travenor.data.model.place.Place
import com.example.travenor.data.place.repository.PlaceRepositoryImpl
import com.example.travenor.data.place.source.local.PlaceExploreLocalSource
import com.example.travenor.data.place.source.local.PlaceLocalDataSource
import com.example.travenor.data.place.source.remote.PlaceRemoteDataSource
import com.example.travenor.data.sharedpreference.SharedPreferencesManager
import com.example.travenor.data.user.repository.UserRepository
import com.example.travenor.data.user.repository.local.UserInterestLocalSource
import com.example.travenor.databinding.FragmentHomeBinding
import com.example.travenor.screen.MainActivity
import com.example.travenor.screen.detail.DetailActivity
import com.example.travenor.screen.home.apdater.PlaceListAdapter
import com.example.travenor.utils.base.BaseFragment
import java.util.Calendar

class HomeFragment :
    BaseFragment<FragmentHomeBinding>(),
    ExplorePlaceContract.View,
    PlaceListAdapter.OnPlaceClickListener {
    private val mAttractionAdapter: PlaceListAdapter by lazy { PlaceListAdapter() }
    private val mRestaurantAdapter: PlaceListAdapter by lazy { PlaceListAdapter() }
    private val mHotelAdapter: PlaceListAdapter by lazy { PlaceListAdapter() }
    private lateinit var mExplorePlacePresenter: ExplorePlacePresenter

    override fun inflateViewBinding(inflater: LayoutInflater): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun initData() {
        val sharedPresenterManager = SharedPreferencesManager.getInstance(context)

        val remoteDataSource = PlaceRemoteDataSource.getInstance()
        val localDataSource = PlaceLocalDataSource.getInstance(requireContext())
        val localExploreDataSource = PlaceExploreLocalSource.getInstance(requireContext())

        val placeRepositoryImpl =
            PlaceRepositoryImpl.getInstance(
                remoteDataSource,
                localDataSource,
                localExploreDataSource
            )
        val userRepository =
            UserRepository.getInstance(UserInterestLocalSource.getInstance(sharedPresenterManager))

        mExplorePlacePresenter = ExplorePlacePresenter(placeRepositoryImpl, userRepository)
        mExplorePlacePresenter.setView(this)

        // Get user interested place type, we'll get Place when it done
        mExplorePlacePresenter.getUserInterest()
    }

    override fun initView() {
        setLayoutBelowSystemBar()
        viewBinding.listAttractionRecycler.adapter = mAttractionAdapter
        viewBinding.listRestaurantRecycler.adapter = mRestaurantAdapter
        viewBinding.listHotelRecycler.adapter = mHotelAdapter

        mAttractionAdapter.setOnPlaceClickListener(this)
        mHotelAdapter.setOnPlaceClickListener(this)
        mRestaurantAdapter.setOnPlaceClickListener(this)

        (activity as MainActivity).setWhiteStatusBar()

        // Greeting header text
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        when {
            hour < MORNING_TIME_POINT -> {
                viewBinding.textGreeting.text = getString(R.string.greeting_morning_text)
                viewBinding.imageDaylightIndicator.setImageDrawable(resources.getDrawable(R.drawable.sun))
            }

            hour < AFTERNOON_TIME_POINT -> {
                viewBinding.textGreeting.text = getString(R.string.greeting_afternoon_text)
                viewBinding.imageDaylightIndicator.setImageDrawable(resources.getDrawable(R.drawable.sun))
            }

            else -> {
                viewBinding.textGreeting.text = getString(R.string.greeting_evening_text)
                viewBinding.imageDaylightIndicator.setImageDrawable(resources.getDrawable(R.drawable.sun))
            }
        }
    }

    private fun setLayoutBelowSystemBar() {
        ViewCompat.setOnApplyWindowInsetsListener(viewBinding.layoutAppBar) { v, insets ->
            val layoutParams = v.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.topMargin = insets.systemWindowInsetTop
            v.layoutParams = layoutParams
            insets
        }
    }

    override fun onGetExplorePlaceSuccess(locationList: List<Place>, placeCategory: PlaceCategory) {
        activity?.runOnUiThread {
            when (placeCategory) {
                PlaceCategory.ATTRACTIONS -> {
                    viewBinding.containerExploreAttractionTitle.visibility = View.VISIBLE
                    mAttractionAdapter.setData(locationList)
                }

                PlaceCategory.RESTAURANTS -> {
                    viewBinding.containerExploreRestaurantTitle.visibility = View.VISIBLE
                    mRestaurantAdapter.setData(locationList)
                }

                PlaceCategory.HOTELS -> {
                    viewBinding.containerExploreHotelTitle.visibility = View.VISIBLE
                    mHotelAdapter.setData(locationList)
                }
            }
        }
    }

    override fun onGetExplorePlaceFail(exception: Exception?, placeCategory: PlaceCategory) {
        exception?.printStackTrace()
        activity?.runOnUiThread {
            when (placeCategory) {
                PlaceCategory.ATTRACTIONS ->
                    viewBinding.containerExploreAttractionTitle.visibility =
                        View.GONE

                PlaceCategory.RESTAURANTS ->
                    viewBinding.containerExploreRestaurantTitle.visibility =
                        View.GONE

                PlaceCategory.HOTELS -> viewBinding.containerExploreHotelTitle.visibility = View.GONE
            }
        }
    }

    override fun onGetPhotoSuccess(photos: PlacePhoto, placeCategory: PlaceCategory) {
        activity?.runOnUiThread {
            val locationId = photos.locationId
            mAttractionAdapter.updateThumbnail(locationId, photos)

            when (placeCategory) {
                PlaceCategory.ATTRACTIONS -> mAttractionAdapter.updateThumbnail(locationId, photos)
                PlaceCategory.RESTAURANTS -> mRestaurantAdapter.updateThumbnail(locationId, photos)
                PlaceCategory.HOTELS -> mHotelAdapter.updateThumbnail(locationId, photos)
            }
        }
    }

    override fun onGetUserInterestPlaceDone() {
        mExplorePlacePresenter.getExploreAttraction()
    }

    override fun onGetUserInterestFoodDone() {
        mExplorePlacePresenter.getExploreRestaurant()

        // TODO Implement explore hotel based on user's location
        mExplorePlacePresenter.getExploreHotel()
    }

    override fun onPlaceClick(locationId: String) {
        val intent = Intent(activity, DetailActivity::class.java)
        intent.putExtra(DetailActivity.KEY_INTENT_PLACE_ID, locationId)
        activity?.startActivity(intent)
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()

        const val MORNING_TIME_POINT = 12
        const val AFTERNOON_TIME_POINT = 18
    }
}
