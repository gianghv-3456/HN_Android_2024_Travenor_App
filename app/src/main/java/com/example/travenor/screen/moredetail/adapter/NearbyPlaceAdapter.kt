package com.example.travenor.screen.moredetail.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.travenor.data.model.photo.PlacePhoto
import com.example.travenor.data.model.place.Place
import com.example.travenor.databinding.ItemEmptySpacingBinding
import com.example.travenor.databinding.ItemNearbyPlaceBinding
import com.example.travenor.utils.ext.loadImageCenterCrop
import com.example.travenor.utils.location.LocationUtils
import kotlin.math.roundToInt

class NearbyPlaceAdapter(context: Context) : RecyclerView.Adapter<NearbyPlaceAdapter.ViewHolder>() {
    private var placeData: List<Place> = emptyList()
    private var placePhotos: MutableList<PlacePhoto> = mutableListOf()
    private var targetLat: Double = 0.0
    private var targetLong: Double = 0.0
    private var onNearbyPlaceClickListener: OnNearbyPlaceClickListener? = null

    fun setTargetLocation(lat: Double, long: Double) {
        targetLat = lat
        targetLong = long
    }

    fun setOnNearbyPlaceClickListener(onNearbyPlaceClickListener: OnNearbyPlaceClickListener) {
        this.onNearbyPlaceClickListener = onNearbyPlaceClickListener
    }

    fun setPlaces(places: List<Place>) {
        placeData = places
        notifyDataSetChanged()
    }

    fun loadImage(photo: List<PlacePhoto>) {
        placePhotos.addAll(photo)
        for (p in photo) {
            var position = 0
            for (place in placeData) {
                if (place.locationId == p.locationId) {
                    position = placeData.indexOf(place) + 1
                    break
                }
            }
            notifyItemChanged(position)
        }
    }

    open class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    class ItemTypeViewHolder(val binding: ItemNearbyPlaceBinding) : ViewHolder(binding.root)
    class EmptySpacingTypeViewHolder(private val binding: ItemEmptySpacingBinding) :
        ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            TYPE_START_EMPTY_SPACING -> {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemEmptySpacingBinding.inflate(layoutInflater, parent, false)
                EmptySpacingTypeViewHolder(binding)
            }

            TYPE_ITEM -> {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemNearbyPlaceBinding.inflate(layoutInflater, parent, false)
                ItemTypeViewHolder(binding)
            }

            else -> {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemEmptySpacingBinding.inflate(layoutInflater, parent, false)
                EmptySpacingTypeViewHolder(binding)
            }
        }
    }

    override fun getItemCount(): Int {
        return placeData.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            TYPE_START_EMPTY_SPACING
        } else {
            TYPE_ITEM
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position > 0) {
            val binding = (holder as ItemTypeViewHolder).binding
            val place = placeData[position - 1]

            binding.textName.text = place.name
            binding.textAddress.text = place.addressObj.getAddress()
            binding.textRating.text = place.rating.toString()

            // Calculate distance. This distance is in meters. Convert it to kilometers.
            val distance: Double = LocationUtils.calculateDistance(
                targetLat,
                targetLong,
                place.latitude.toDouble(),
                place.longitude.toDouble()
            ) * METERS_TO_KILOMETERS

            val roundedDistance: Int = distance.roundToInt()
            binding.textDistance.text = "$roundedDistance km"

            for (photo in placePhotos) {
                if (photo.locationId == place.locationId) {
                    // Set image.

                    val imageUrl = photo.imageList.getBiggestImageAvailable()?.url
                    binding.imageThumbnailPlace.loadImageCenterCrop(imageUrl.toString())
                    break
                }
            }

            holder.itemView.setOnClickListener {
                onNearbyPlaceClickListener?.onNearbyPlaceClick(place.locationId)
            }
        }
    }

    interface OnNearbyPlaceClickListener {
        fun onNearbyPlaceClick(placeId: String)
    }

    companion object {
        private const val TYPE_START_EMPTY_SPACING = 0
        private const val TYPE_ITEM = 1
        private const val METERS_TO_KILOMETERS = 0.001
    }
}
