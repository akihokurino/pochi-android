package com.wanpaku.pochi.infra.ui.widget

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

fun ImageView.load(url: String, requestOptions: RequestOptions = RequestOptions.centerCropTransform()) {
    Glide.with(this).load(url)
            .apply(requestOptions)
            .into(this)
}