package com.example.travenor.utils.ext

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.travenor.R

fun ImageView.loadImageCircleWithUrl(url: String) {
    Glide.with(this).load(url).circleCrop().into(this)
}

fun ImageView.loadImageWithUrl(url: String) {
    Glide.with(this).load(url).into(this)
}

fun ImageView.loadImageCenterCrop(url: String) {
    Glide.with(this).load(url).placeholder(R.drawable.sample_vinh_ha_long).centerCrop().into(this)
}
