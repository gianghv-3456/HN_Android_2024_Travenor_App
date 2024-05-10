package com.example.travenor.screen.favorite.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.travenor.data.model.photo.PlacePhoto
import com.example.travenor.data.model.place.Place
import com.example.travenor.databinding.ItemNoPlaceBinding
import com.example.travenor.databinding.ItemPlaceSingleListBinding
import com.example.travenor.utils.ext.loadImageCenterCrop

class FavoriteListAdapter : RecyclerView.Adapter<FavoriteListAdapter.ViewHolder>() {
    private val data: MutableList<Place> = mutableListOf()
    private var thumbnailPhotoMap = mutableMapOf<String, PlacePhoto>()
    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    fun updateData(newData: List<Place>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    fun loadImages(locationId: String, photo: PlacePhoto) {
        thumbnailPhotoMap[locationId] = photo
        notifyDataSetChanged()
    }

    fun removePlace(placeId: String) {
        data.forEachIndexed { index, place ->
            if (place.locationId == placeId) {
                data.remove(place)
                notifyItemRemoved(index)
                return
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (data.isEmpty()) {
            VIEW_TYPE_NO_DATA
        } else {
            VIEW_TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_NO_DATA -> NoPlaceViewHolder(
                ItemNoPlaceBinding.inflate(
                    inflater,
                    parent,
                    false
                )
            )

            VIEW_TYPE_ITEM -> PlaceViewHolder(
                ItemPlaceSingleListBinding.inflate(
                    inflater,
                    parent,
                    false
                )
            )

            else -> NoPlaceViewHolder(ItemNoPlaceBinding.inflate(inflater, parent, false))
        }
    }

    override fun getItemCount(): Int {
        return if (data.isEmpty()) 1 else data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (getItemViewType(position) == VIEW_TYPE_ITEM) {
            val place = data[position]
            val binding = (holder as PlaceViewHolder).binding
            binding.textName.text = place.name
            binding.textAddress.text = place.addressObj.getAddress()
            binding.textRating.text = place.rating.toString()
            binding.textRatingAmount.text = place.ratingAmount.toString()

            val photo = thumbnailPhotoMap[place.locationId]
            if (photo != null) {
                val imageUrl = photo.imageList.getBiggestImageAvailable()?.url.toString()
                binding.imageThumbnailPlace.loadImageCenterCrop(imageUrl)
            }

            binding.root.setOnClickListener {
                onItemClickListener?.onPlaceClick(place.locationId)
            }
            binding.buttonRemoveFavorite.setOnClickListener {
                onItemClickListener?.onRemovePlaceClick(place.locationId)
            }
        }
    }

    interface OnItemClickListener {
        fun onPlaceClick(locationId: String)
        fun onRemovePlaceClick(locationId: String)
    }

    open class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    class NoPlaceViewHolder(binding: ItemNoPlaceBinding) : ViewHolder(binding.root)
    class PlaceViewHolder(val binding: ItemPlaceSingleListBinding) : ViewHolder(binding.root)

    companion object {
        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_NO_DATA = 1
    }
}
