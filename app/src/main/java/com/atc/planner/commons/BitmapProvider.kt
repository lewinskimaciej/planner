package com.atc.planner.commons

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import com.atc.planner.App
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import javax.inject.Inject

interface BitmapProvider {
    fun getRoundedBitmap(url: String?, @DrawableRes errorDrawable: Int, onSuccess: ((Bitmap?) -> Unit)?, onFailure: ((Bitmap?) -> Unit)?)
}

class BitmapProviderImpl @Inject constructor(val app: App) : BitmapProvider {

    override fun getRoundedBitmap(url: String?, @DrawableRes errorDrawable: Int, onSuccess: ((Bitmap?) -> Unit)?, onFailure: ((Bitmap?) -> Unit)?) {
        Glide.with(app)
                .asBitmap()
                .load(url)
                .apply(RequestOptions().circleCrop())
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap?, transition: Transition<in Bitmap>?) {
                        onSuccess?.invoke(resource)
                    }

                    override fun onLoadFailed(error: Drawable?) {
                        onFailure?.invoke(BitmapFactory.decodeResource(app.resources, errorDrawable))
                    }
                })

    }
}
