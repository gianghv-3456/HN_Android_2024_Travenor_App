package com.example.travenor.screen.detail

import android.content.res.Resources
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.ViewCompat
import com.example.travenor.R
import com.example.travenor.data.model.photo.PlacePhoto
import com.example.travenor.data.model.place.Place
import com.example.travenor.data.place.repository.PlaceRepository
import com.example.travenor.data.place.source.remote.PlaceRemoteDataSource
import com.example.travenor.databinding.ActivityDetailBinding
import com.example.travenor.utils.base.BaseActivity
import com.example.travenor.utils.ext.loadImageCenterCrop
import com.google.android.material.bottomsheet.BottomSheetBehavior

class DetailActivity : BaseActivity(), DetailContract.View {
    private lateinit var mBinding: ActivityDetailBinding
    private var mPlaceId: String = ""
    private var mPresenter: DetailPresenter? = null

    override fun getLayoutRoot(): View {
        mBinding = ActivityDetailBinding.inflate(layoutInflater)
        return mBinding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun initView() {
        setLayoutBelowSystemBar()
        if (!intent.hasExtra(KEY_INTENT_PLACE_ID)) finish()

        mPlaceId = intent.getStringExtra(KEY_INTENT_PLACE_ID).toString()

        mPresenter =
            DetailPresenter(PlaceRepository.getInstance(PlaceRemoteDataSource.getInstance()))
        mPresenter?.setView(this)

        mBinding.buttonBack.setOnClickListener { _ -> finish() }
        mBinding.buttonMoreDetail.setOnClickListener { _ -> openMoreDetailScreen() }
        mBinding.buttonMarkFavorite.setOnClickListener { _ -> markFavorite() }

        initBottomSheet()
    }

    private fun initBottomSheet() {
        val bottomBehavior = BottomSheetBehavior.from(mBinding.containerBottomSheet)
        bottomBehavior.peekHeight = ((Resources.getSystem().displayMetrics.heightPixels) * FIFTY_PERCENT).toInt()
        bottomBehavior.maxHeight = ((Resources.getSystem().displayMetrics.heightPixels) * EIGHTY_PERCENT).toInt()
        bottomBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    override fun initData() {
        super.initData()
        mPresenter?.getPlaceDetail(mPlaceId)
        mPresenter?.getPlacePhotos(mPlaceId)
    }

    private fun openMoreDetailScreen() {
        // TODO OPEN MORE DETAIL SCREEN
    }

    private fun markFavorite() {
        // TODO Integrate Favorite future
    }

    override fun onGetPlaceDetailSuccess(place: Place) {
        runOnUiThread {
            mBinding.textPlaceName.text = place.name
            mBinding.textPlaceDesc.text = place.description
            mBinding.textPlaceAddress.text = place.addressObj.addressString
            mBinding.textPlaceRating.text = place.rating.toString()
        }
    }

    override fun onGetPlaceDetailFail(e: Exception) {
        runOnUiThread {
            Toast.makeText(
                this,
                getString(R.string.detail_get_detail_fail_toast),
                Toast.LENGTH_SHORT
            ).show()
        }
        finish()
    }

    override fun onGetPhotoSuccess(photos: PlacePhoto) {
        runOnUiThread {
            mBinding.imagePlaceHeader.loadImageCenterCrop(photos.imageList.original.url)
        }
    }

    private fun setLayoutBelowSystemBar() {
        ViewCompat.setOnApplyWindowInsetsListener(mBinding.layoutAppBar) { v, insets ->
            val layoutParams = v.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.topMargin = insets.systemWindowInsetTop
            v.layoutParams = layoutParams
            insets
        }
    }

    companion object {
        const val KEY_INTENT_PLACE_ID = "activity.detail.key.intent_place_id"
        private const val EIGHTY_PERCENT = 0.8
        private const val FIFTY_PERCENT = 0.5
    }
}
