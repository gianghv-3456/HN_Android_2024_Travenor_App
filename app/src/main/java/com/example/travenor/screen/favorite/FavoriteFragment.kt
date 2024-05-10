package com.example.travenor.screen.favorite

import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.ViewCompat
import com.example.travenor.data.model.photo.PlacePhoto
import com.example.travenor.data.model.place.Place
import com.example.travenor.data.place.repository.PlaceRepositoryImpl
import com.example.travenor.data.place.source.local.PlaceExploreLocalSource
import com.example.travenor.data.place.source.local.PlaceLocalDataSource
import com.example.travenor.data.place.source.remote.PlaceRemoteDataSource
import com.example.travenor.databinding.FragmentFavoriteBinding
import com.example.travenor.screen.favorite.adapter.FavoriteListAdapter
import com.example.travenor.screen.moredetail.MoreDetailActivity
import com.example.travenor.utils.base.BaseFragment

class FavoriteFragment :
    BaseFragment<FragmentFavoriteBinding>(),
    FavoriteContract.View,
    FavoriteListAdapter.OnItemClickListener {
    private val mAdapter: FavoriteListAdapter by lazy { FavoriteListAdapter() }
    private lateinit var mPresenter: FavoritePresenter

    override fun inflateViewBinding(inflater: LayoutInflater): FragmentFavoriteBinding {
        return FragmentFavoriteBinding.inflate(inflater)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun initData() {
        val remoteDataSource = PlaceRemoteDataSource.getInstance()
        val localDataSource = PlaceLocalDataSource.getInstance(requireContext())
        val localExploreDataSource = PlaceExploreLocalSource.getInstance(requireContext())
        val placeRepositoryImpl = PlaceRepositoryImpl.getInstance(
            remoteDataSource,
            localDataSource,
            localExploreDataSource
        )
        mPresenter = FavoritePresenter(placeRepositoryImpl)
        mPresenter.setView(this)
        mPresenter.getFavoritePlaces()
    }

    override fun initView() {
        setLayoutBelowSystemBar()
        viewBinding.recyclerView.adapter = mAdapter
        mAdapter.setOnItemClickListener(this)
    }

    private fun setLayoutBelowSystemBar() {
        ViewCompat.setOnApplyWindowInsetsListener(viewBinding.layoutAppBar) { v, insets ->
            val layoutParams = v.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.topMargin = insets.systemWindowInsetTop
            v.layoutParams = layoutParams
            insets
        }
    }

    override fun onGetFavoritePlacesSuccess(places: List<Place>) {
        // TODO remove runOnUiThread
        activity?.runOnUiThread {
            mAdapter.updateData(places)
        }
        for (place in places) {
            mPresenter.getPlacePhoto(place.locationId)
        }
    }

    override fun onGetFavoritePlacesFailed(message: String) {
        // TODO remove runOnUiThread
        activity?.runOnUiThread { mAdapter.notifyDataSetChanged() }
    }

    override fun onGetPlacePhotoSuccess(placeId: String, photos: List<PlacePhoto>) {
        // TODO remove runOnUiThread
        activity?.runOnUiThread { mAdapter.loadImages(placeId, photos[0]) }
    }

    override fun onRemoveFavoritePlaceSuccess(placeId: String) {
        // TODO remove runOnUiThread
        activity?.runOnUiThread {
            mAdapter.removePlace(placeId)
        }
    }

    override fun onPlaceClick(locationId: String) {
        val intent = Intent(activity, MoreDetailActivity::class.java)
        intent.putExtra(MoreDetailActivity.EXTRA_PLACE_ID, locationId)
        intent.putExtra(
            MoreDetailActivity.EXTRA_ACTIVITY_OPEN_MODE,
            MoreDetailActivity.MODE_DISPLAY_NEARBY_PLACE
        )
        startActivity(intent)
    }

    override fun onRemovePlaceClick(locationId: String) {
        mPresenter.removeFavoritePlace(locationId)
    }

    companion object {
        @JvmStatic
        fun newInstance() = FavoriteFragment()
    }
}
