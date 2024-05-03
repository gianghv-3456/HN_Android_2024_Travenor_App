package com.example.travenor.utils.ext

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.travenor.R
import com.example.travenor.utils.network.NetworkUtils

fun ImageView.loadImageCircleWithUrl(url: String) {
    Glide.with(this).load(url).circleCrop().into(this)
}

fun ImageView.loadImageWithUrl(url: String) {
    Glide.with(this).load(url).into(this)
}

fun ImageView.loadImageCenterCrop(url: String) {
    var requestOptions: RequestOptions = RequestOptions()
        .placeholder(R.drawable.sample_vinh_ha_long) // placeholder image
        .error(R.drawable.ic_no_image) // error image if none found in cache
        .diskCacheStrategy(DiskCacheStrategy.ALL) // Cache strategy

    if (!NetworkUtils.isNetworkAvailable(this.context)) {
        requestOptions = requestOptions.onlyRetrieveFromCache(true)
    }
    Glide.with(this).load(url).apply(requestOptions).centerCrop().into(this)
}
