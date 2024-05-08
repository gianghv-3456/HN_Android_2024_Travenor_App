package com.example.travenor.screen.popup.imageview.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.travenor.R
import com.example.travenor.data.model.photo.PlacePhoto
import com.example.travenor.utils.ext.loadImageCenterCrop

class ImageGalleryAdapter(
    private val images: List<PlacePhoto>,
    private val listener: OnImageSelectedListener
) : RecyclerView.Adapter<ImageGalleryAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.image_place_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val view = inflater.inflate(R.layout.item_image_gallery_view_popup, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageUrl: String = images[position].imageList.getBiggestImageAvailable()?.url.toString()

        holder.imageView.loadImageCenterCrop(imageUrl)
        holder.itemView.setOnClickListener { _ ->
            listener.onImageSelected(position)
        }
    }

    interface OnImageSelectedListener {
        fun onImageSelected(position: Int)
    }
}
