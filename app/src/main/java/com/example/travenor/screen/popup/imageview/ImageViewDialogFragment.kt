package com.example.travenor.screen.popup.imageview

import android.app.Dialog
import android.os.Bundle
import android.view.Window
import android.widget.ImageButton
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.example.travenor.R
import com.example.travenor.data.model.photo.PlacePhoto
import com.example.travenor.screen.popup.imageview.adapter.ImageGalleryAdapter
import com.example.travenor.utils.ext.loadImageCenterCrop

class ImageViewDialogFragment(
    private val images: List<PlacePhoto>,
    private var selectedPosition: Int = 0
) : DialogFragment(), ImageGalleryAdapter.OnImageSelectedListener {
    private lateinit var dialog: Dialog
    private var imageContent: ImageView? = null
    private lateinit var adapter: ImageGalleryAdapter

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        adapter = ImageGalleryAdapter(images, this)

        dialog = Dialog(requireContext(), R.style.Theme_Androidtemplate_Popup)

        with(dialog) {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.layout_popup_image_view)
            show()
        }

        imageContent = dialog.findViewById(R.id.image_content)
        loadImageContent(selectedPosition)

        val recyclerView = dialog.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView?.adapter = this.adapter

        dialog.findViewById<ImageButton>(R.id.button_dismiss)
            ?.setOnClickListener { _ -> dialog.dismiss() }

        return dialog
    }

    override fun onImageSelected(position: Int) {
        selectedPosition = position
        loadImageContent(position)
    }

    private fun loadImageContent(position: Int) {
        val image = images[position]

        val imageUrl: String = image.imageList.getBiggestImageAvailable()?.url.toString()
        imageContent?.loadImageCenterCrop(imageUrl)
    }

    companion object {
        const val TAG = "ImageViewDialogFragment"
        fun newInstance(images: List<PlacePhoto>, position: Int): ImageViewDialogFragment {
            return ImageViewDialogFragment(images, position)
        }
    }
}
