package com.example.travenor.screen.nearby.adapter

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.travenor.R
import com.example.travenor.data.model.photo.PlacePhoto
import com.example.travenor.data.model.place.Place
import com.example.travenor.databinding.LayoutCustomInfoWindowBinding
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.model.Marker

class CustomInfoWindowAdapter(
    val inflater: LayoutInflater
) : InfoWindowAdapter {
    private val placeList = mutableListOf<Place>()
    private val photoList = mutableListOf<PlacePhoto>()

    fun addData(placeList: List<Place>, photoList: List<PlacePhoto>) {
        this.placeList.addAll(placeList)
        this.photoList.addAll(photoList)
    }

    fun clearData() {
        this.placeList.clear()
        this.photoList.clear()
    }

    override fun getInfoContents(p0: Marker): View? {
        return null
    }

    @SuppressLint("SetTextI18n")
    override fun getInfoWindow(p0: Marker): View? {
        if (p0.tag.toString() == TAG_NO_DISPLAY) {
            return null
        }

        val binding = LayoutCustomInfoWindowBinding.inflate(inflater)

        val place = placeList.find { it.locationId == p0.snippet }
        val photo = photoList.find { it.locationId == p0.snippet }

        binding.textName.text = place?.name
        binding.textAddress.text = place?.addressObj?.getAddress().toString()
        binding.textRating.text = "Rating: ${place?.rating}"
        binding.textRatingAmount.text = "(${place?.ratingAmount})"

        val imageUrl: String = photo?.imageList?.getBiggestImageAvailable()?.url.toString()

        Glide.with(inflater.context).load(imageUrl)
            .transition(DrawableTransitionOptions.withCrossFade()).listener(MarkerCallback(p0))
            .override(THUMBNAIL_SIZE)
            .centerCrop().error(R.drawable.ic_no_image).into(binding.imageThumbnailPlace)

        return binding.root
    }

    class MarkerCallback internal constructor(marker: Marker?) : RequestListener<Drawable> {
        private var marker: Marker? = null
        private fun onSuccess() {
            if (marker != null && marker!!.isInfoWindowShown) {
                marker!!.hideInfoWindow()
                marker!!.showInfoWindow()
            }
        }

        init {
            this.marker = marker
        }

        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>,
            isFirstResource: Boolean
        ): Boolean {
            Log.e(javaClass.simpleName, "Error loading thumbnail! -> $e")
            return false
        }

        override fun onResourceReady(
            resource: Drawable,
            model: Any,
            target: Target<Drawable>?,
            dataSource: DataSource,
            isFirstResource: Boolean
        ): Boolean {
            onSuccess()
            return false
        }
    }

    companion object {
        const val TAG_NO_DISPLAY = "no_display"
        const val TAG_DISPLAY = "display"
        const val THUMBNAIL_SIZE = 50
    }
}
