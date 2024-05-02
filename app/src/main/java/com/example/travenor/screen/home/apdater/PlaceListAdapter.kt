package com.example.travenor.screen.home.apdater

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.travenor.data.model.photo.PlacePhoto
import com.example.travenor.data.model.place.Place
import com.example.travenor.databinding.ItemPlaceBinding
import com.example.travenor.utils.ext.loadImageCenterCrop
import com.example.travenor.utils.ext.notNull

class PlaceListAdapter : RecyclerView.Adapter<PlaceListAdapter.ViewHolder>() {
    private var placeList: MutableList<Place> = mutableListOf()
    private var thumbnailPhotoMap = mutableMapOf<String, PlacePhoto>()
    private var listener: OnPlaceClickListener? = null

    fun setData(placeList: List<Place>) {
        placeList.notNull {
            this.placeList.clear()
            this.placeList.addAll(it)
            notifyDataSetChanged()
        }
    }

    fun setOnPlaceClickListener(listener: OnPlaceClickListener) {
        this.listener = listener
    }

    fun updateThumbnail(locationId: String, photo: PlacePhoto) {
        thumbnailPhotoMap[locationId] = photo
        placeList.forEachIndexed { index, _ ->
            notifyItemChanged(index)
        }
    }

    class ViewHolder(val binding: ItemPlaceBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val viewBinding = ItemPlaceBinding.inflate(layoutInflater)
        return ViewHolder(viewBinding)
    }

    override fun getItemCount(): Int {
        return placeList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = placeList[position]

        holder.binding.textPlaceTitle.text = place.name
        holder.binding.textPlaceRating.text = place.rating.toString()
        holder.binding.textPlaceAddress.text = place.addressObj.addressString

        // set thumbnail
        val id = place.locationId
        val url =
            if (thumbnailPhotoMap.containsKey(id)) thumbnailPhotoMap[id]?.imageList?.original?.url else ""
        holder.binding.imageThumbnailPlace.loadImageCenterCrop(url.toString())

        holder.itemView.setOnClickListener { _ -> listener?.onPlaceClick(place.locationId) }
    }

    interface OnPlaceClickListener {
        fun onPlaceClick(locationId: String)
    }
}
