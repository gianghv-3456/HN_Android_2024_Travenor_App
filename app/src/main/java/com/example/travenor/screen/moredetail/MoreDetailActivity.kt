package com.example.travenor.screen.moredetail

import android.content.Intent
import android.os.Build
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.ViewCompat
import com.example.travenor.constant.PlaceCategory
import com.example.travenor.data.model.photo.PlacePhoto
import com.example.travenor.data.model.place.Place
import com.example.travenor.data.place.repository.NearbyRepositoryImpl
import com.example.travenor.data.place.repository.PlaceRepositoryImpl
import com.example.travenor.data.place.source.local.PlaceExploreLocalSource
import com.example.travenor.data.place.source.local.PlaceLocalDataSource
import com.example.travenor.data.place.source.remote.PlaceRemoteDataSource
import com.example.travenor.databinding.ActivityMoreDetailBinding
import com.example.travenor.screen.moredetail.adapter.NearbyPlaceAdapter
import com.example.travenor.screen.popup.imageview.ImageViewDialogFragment
import com.example.travenor.utils.base.BaseActivity
import com.example.travenor.utils.ext.loadImageCenterCrop

@Suppress("TooManyFunctions")
class MoreDetailActivity : BaseActivity(), MoreDetailContract.View, NearbyPlaceAdapter.OnNearbyPlaceClickListener {
    private var placeId = ""
    private var photos: List<PlacePhoto> = emptyList()

    private val mNearbyRestaurantAdapter: NearbyPlaceAdapter by lazy { NearbyPlaceAdapter(this) }
    private val mNearbyHotelAdapter: NearbyPlaceAdapter by lazy { NearbyPlaceAdapter(this) }

    private var mActivityOpenMode = MODE_NON_DISPLAY_NEARBY_PLACE

    private lateinit var mPresenter: MoreDetailPresenter
    private lateinit var mBinding: ActivityMoreDetailBinding

    override fun getLayoutRoot(): View {
        mBinding = ActivityMoreDetailBinding.inflate(layoutInflater)
        return mBinding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun initData() {
        placeId = intent?.getStringExtra(EXTRA_PLACE_ID).toString()

        if (placeId.isEmpty()) {
            finish()
        }

        val remoteDataSource = PlaceRemoteDataSource.getInstance()
        val localDataSource = PlaceLocalDataSource.getInstance(baseContext)
        val localExploreDataSource = PlaceExploreLocalSource.getInstance(baseContext)

        val placeRepositoryImpl = PlaceRepositoryImpl.getInstance(
            remoteDataSource,
            localDataSource,
            localExploreDataSource
        )

        val nearbyRepositoryImpl = NearbyRepositoryImpl.getInstance(
            remoteDataSource,
            localDataSource
        )

        mPresenter = MoreDetailPresenter(placeRepositoryImpl, nearbyRepositoryImpl)
        mPresenter.setView(this)

        mPresenter.getPlaceDetail(placeId)
        mPresenter.getPlacePhotos(placeId)
    }

    override fun initView() {
        setLayoutBelowSystemBar()
        setWhiteStatusBar()

        mActivityOpenMode = intent.getIntExtra(EXTRA_ACTIVITY_OPEN_MODE, MODE_NON_DISPLAY_NEARBY_PLACE)

        mBinding.buttonBack.setOnClickListener { _ -> onBackPressed() }

        mNearbyHotelAdapter.setOnNearbyPlaceClickListener(this)
        mNearbyRestaurantAdapter.setOnNearbyPlaceClickListener(this)

        val firstPositionToOpen = 0
        val secondPositionToOpen = 1
        val thirdPositionToOpen = 2
        mBinding.imagePlaceImage1.setOnClickListener { _ ->
            openPlaceImageGalleryView(
                firstPositionToOpen
            )
        }
        mBinding.imagePlaceImage2.setOnClickListener { _ ->
            openPlaceImageGalleryView(
                secondPositionToOpen
            )
        }
        mBinding.imagePlaceImage3.setOnClickListener { _ ->
            openPlaceImageGalleryView(
                thirdPositionToOpen
            )
        }

        mBinding.recyclerNearbyRestaurant.adapter = mNearbyRestaurantAdapter
        mBinding.recyclerNearbyHotel.adapter = mNearbyHotelAdapter
    }

    private fun setLayoutBelowSystemBar() {
        ViewCompat.setOnApplyWindowInsetsListener(mBinding.layoutAppBar) { v, insets ->
            val layoutParams = v.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.topMargin = insets.systemWindowInsetTop
            v.layoutParams = layoutParams
            insets
        }
    }

    override fun onGetPlaceDetailSuccess(place: Place) {
        runOnUiThread {
            mBinding.textPlaceName.text = place.name
            mBinding.textDesc.text = place.description
            mBinding.textPlaceAddress.text = place.addressObj.getAddress()
            mBinding.textRating.text = place.rating.toString()
            mBinding.textRatingAmount.text = place.ratingAmount.toString()
            mBinding.textTitle.text = place.name
            mNearbyHotelAdapter.setTargetLocation(
                place.latitude.toDouble(),
                place.longitude.toDouble()
            )
            mNearbyRestaurantAdapter.setTargetLocation(
                place.latitude.toDouble(),
                place.longitude.toDouble()
            )
        }

        if (mActivityOpenMode == MODE_DISPLAY_NEARBY_PLACE) {
            mPresenter.getNearbyRestaurant(place.latitude.toDouble(), place.longitude.toDouble())
            mPresenter.getNearbyHotel(place.latitude.toDouble(), place.longitude.toDouble())
        }
    }

    override fun onGetPlaceDetailFail(e: Exception) {
        finish()
    }

    private fun openPlaceImageGalleryView(position: Int) {
        val imageViewDialogFragment = ImageViewDialogFragment.newInstance(photos, position)
        imageViewDialogFragment.show(supportFragmentManager, ImageViewDialogFragment.TAG)
    }

    override fun onGetNearbyRestaurantSuccess(place: List<Place>) {
        runOnUiThread {
            if (mActivityOpenMode == MODE_DISPLAY_NEARBY_PLACE) {
                mBinding.recyclerNearbyRestaurant.visibility = View.VISIBLE
                mBinding.textLabelNearbyRestaurant.visibility = View.VISIBLE
            }
            mNearbyRestaurantAdapter.setPlaces(place)
        }
    }

    override fun onGetNearbyHotelSuccess(places: List<Place>) {
        runOnUiThread {
            if (mActivityOpenMode == MODE_DISPLAY_NEARBY_PLACE) {
                mBinding.recyclerNearbyHotel.visibility = View.VISIBLE
                mBinding.textLabelNearbyHotel.visibility = View.VISIBLE
            }
            mNearbyHotelAdapter.setPlaces(places)
        }
    }

    override fun onGetNearbyPlacePhotoSuccess(photos: List<PlacePhoto>, category: PlaceCategory) {
        runOnUiThread {
            when (category) {
                PlaceCategory.RESTAURANTS -> {
                    mNearbyRestaurantAdapter.loadImage(photos)
                }

                PlaceCategory.HOTELS -> {
                    mNearbyHotelAdapter.loadImage(photos)
                }

                else -> {
                    // do nothing
                }
            }
        }
    }

    override fun onGetPhotoSuccess(photos: List<PlacePhoto>) {
        this.photos = photos
        runOnUiThread {
            initGalleryPreview(photos)
        }
    }

    private fun initGalleryPreview(photos: List<PlacePhoto>) {
        val imageCount = photos.size
        mBinding.cardGallery3.visibility = View.GONE
        mBinding.cardGallery1.visibility = View.GONE
        mBinding.cardGallery2.visibility = View.GONE
        mBinding.textImageAmountHint.visibility = View.GONE

        when (imageCount) {
            NO_IMAGE_HOLDER -> {
                // No images
            }

            ONE_IMAGE_HOLDER -> {
                // only one image
                mBinding.cardGallery1.visibility = View.VISIBLE
            }

            TWO_IMAGE_HOLDER -> {
                // two images
                mBinding.cardGallery1.visibility = View.VISIBLE
                mBinding.cardGallery2.visibility = View.VISIBLE
            }

            THREE_IMAGE_HOLDER -> {
                // three images
                mBinding.cardGallery1.visibility = View.VISIBLE
                mBinding.cardGallery2.visibility = View.VISIBLE
                mBinding.cardGallery3.visibility = View.VISIBLE
            }

            else -> {
                // more than three images, display number of images hint
                mBinding.cardGallery1.visibility = View.VISIBLE
                mBinding.cardGallery2.visibility = View.VISIBLE
                mBinding.cardGallery3.visibility = View.VISIBLE
                mBinding.textImageAmountHint.visibility = View.VISIBLE
            }
        }

        val numberOfHiddenPhotos = imageCount - THREE_IMAGE_HOLDER
        if (numberOfHiddenPhotos > 0) {
            mBinding.textImageAmountHint.text = "+$numberOfHiddenPhotos"
        }

        for (i in 0 until imageCount) {
            val imageView = when (i) {
                0 -> mBinding.imagePlaceImage1
                1 -> mBinding.imagePlaceImage2
                2 -> mBinding.imagePlaceImage3
                else -> null
            }
            val imageUrl: String = photos[i].imageList.getBiggestImageAvailable()?.url.toString()
            imageView?.loadImageCenterCrop(imageUrl)
        }

        // Header
        val imageUrl: String = photos.first().imageList.getBiggestImageAvailable()?.url.toString()
        mBinding.imageHeader.loadImageCenterCrop(imageUrl)
    }

    override fun onNearbyPlaceClick(placeId: String) {
        val intent = Intent(this, MoreDetailActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra(EXTRA_PLACE_ID, placeId)
        intent.putExtra(EXTRA_ACTIVITY_OPEN_MODE, MODE_NON_DISPLAY_NEARBY_PLACE)
        startActivity(intent)
    }

    companion object {
        const val EXTRA_PLACE_ID = "extra_hotel_id"
        private const val NO_IMAGE_HOLDER = 0
        private const val ONE_IMAGE_HOLDER = 1
        private const val TWO_IMAGE_HOLDER = 2
        private const val THREE_IMAGE_HOLDER = 3

        /**
         * Open mode for [MoreDetailActivity]
         * DISPLAY_NEARBY_PLACE: 1 - Display nearby restaurant and hotel
         * NON_DISPLAY_NEARBY_PLACE: 2 - Do not display nearby restaurant and hotel
         */
        const val MODE_DISPLAY_NEARBY_PLACE = 1
        const val MODE_NON_DISPLAY_NEARBY_PLACE = 2
        const val EXTRA_ACTIVITY_OPEN_MODE = "extra_activity_open_mode"
    }
}
