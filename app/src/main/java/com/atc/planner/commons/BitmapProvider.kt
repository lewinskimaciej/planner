package com.atc.planner.commons

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.atc.planner.App
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import javax.inject.Inject

interface BitmapProvider {
    fun getRoundedBitmap(url: String?, onSuccess: ((Bitmap?) -> Unit)?, onFailure: ((Drawable?) -> Unit)?)
}

class BitmapProviderImpl @Inject constructor(val app: App) : BitmapProvider {

    override fun getRoundedBitmap(url: String?, onSuccess: ((Bitmap?) -> Unit)?, onFailure: ((Drawable?) -> Unit)?) {
        Glide.with(app)
                .asBitmap()
                .load(url)
                .apply(RequestOptions().circleCrop())
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap?, transition: Transition<in Bitmap>?) {
                        onSuccess?.invoke(resource)
                    }

                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        onFailure?.invoke(errorDrawable)
                    }
                })

    }
}
