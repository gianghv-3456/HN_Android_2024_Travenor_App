package com.example.travenor.screen.search.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.travenor.data.model.photo.PlacePhoto
import com.example.travenor.data.model.place.Place
import com.example.travenor.databinding.ItemPlaceSingleListBinding
import com.example.travenor.databinding.ItemSearchNoResultBinding
import com.example.travenor.utils.ext.loadImageCenterCrop

class SearchResultAdapter : RecyclerView.Adapter<SearchResultAdapter.ViewHolder>() {
    private val mSearchResultList = mutableListOf<Place>()
    private var mThumbnailPhotoMap = mutableMapOf<String, PlacePhoto?>()
    private var mListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    fun setSearchResultList(searchResultList: List<Place>) {
        mSearchResultList.clear()
        mSearchResultList.addAll(searchResultList)
        mSearchResultList.forEach {
            // init thumbnail photo map
            mThumbnailPhotoMap[it.locationId] = null
        }
        notifyDataSetChanged()
    }

    fun clearSearchResultList() {
        mSearchResultList.clear()
        mThumbnailPhotoMap.clear()
        notifyDataSetChanged()
    }

    fun addSearchResult(place: Place) {
        mSearchResultList.add(place)
        mThumbnailPhotoMap[place.locationId] = null
        notifyItemInserted(mSearchResultList.size)
    }

    fun setThumbnailPhotoMap(placeId: String, thumbnailPhoto: PlacePhoto) {
        if (mThumbnailPhotoMap.containsKey(placeId)) {
            mThumbnailPhotoMap[placeId] = thumbnailPhoto
        }
        val position = mSearchResultList.indexOfFirst { it.locationId == placeId }
        notifyItemChanged(position)
    }

    open class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView)

    class ResultViewHolder(val binding: ItemPlaceSingleListBinding) : ViewHolder(binding.root)
    class NoResultViewHolder(binding: ItemSearchNoResultBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_RESULT -> {
                ResultViewHolder(ItemPlaceSingleListBinding.inflate(inflater, parent, false))
            }

            else -> {
                NoResultViewHolder(ItemSearchNoResultBinding.inflate(inflater, parent, false))
            }
        }
    }

    override fun getItemCount(): Int {
        return if (mSearchResultList.isEmpty()) 1 else mSearchResultList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (getItemViewType(position) == TYPE_RESULT) {
            val place = mSearchResultList[position]
            val binding = (holder as ResultViewHolder).binding

            val photo = mThumbnailPhotoMap[place.locationId]
            if (photo != null) {
                val imageUrl = photo.imageList.getBiggestImageAvailable()?.url.toString()
                binding.imageThumbnailPlace.loadImageCenterCrop(imageUrl)
            }

            binding.textName.text = place.name
            binding.textAddress.text = place.addressObj.getAddress()
            binding.textRating.text = place.rating.toString()
            binding.textRatingAmount.text = place.ratingAmount.toString()

            binding.buttonRemoveFavorite.visibility = View.GONE

            binding.root.setOnClickListener {
                mListener?.onPlaceClick(place.locationId)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (mSearchResultList.isEmpty()) return TYPE_NO_RESULT
        return TYPE_RESULT
    }

    interface OnItemClickListener {
        fun onPlaceClick(placeId: String)
    }

    companion object {
        const val TYPE_RESULT = 1
        const val TYPE_NO_RESULT = 2
    }
}