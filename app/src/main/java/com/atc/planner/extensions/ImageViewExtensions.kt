package com.atc.planner.extensions

import android.graphics.Bitmap
import android.net.Uri
import android.support.annotation.IdRes
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import java.io.File

fun ImageView.loadImage(url: String?, bitmapTransformation: Transformation<Bitmap>? = null) {
    url?.let {
        if (bitmapTransformation != null) {
            Glide.with(this.context)
                    .load(url)
                    .bitmapTransform(bitmapTransformation)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(this)
        } else {
            Glide.with(this.context)
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(this)
        }
    }
}

fun ImageView.loadImage(url: String?, @IdRes error: Int?) {
    url?.let {
        if (error != null) {
            Glide.with(this.context)
                    .load(url)
                    .error(error)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(this)
        }
    }
}

fun ImageView.loadImage(file: File?) {
    file?.let {
        Glide.with(this.context)
                .load(file)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(this)
    }
}

fun ImageView.loadImage(file: File?, @IdRes error: Int?) {
    file?.let {
        if (error != null) {
            Glide.with(this.context)
                    .load(file)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(error)
                    .into(this)
        }
    }
}

fun ImageView.loadImage(uri: Uri?) {
    uri?.let {
        Glide.with(this.context)
                .load(uri)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(this)
    }
}

fun ImageView.loadImage(@IdRes drawable: Int?, @IdRes error: Int?) {
    drawable?.let {
        if (error != null) {
            Glide.with(this.context)
                    .load(drawable)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(error)
                    .into(this)
        }
    }
}

fun ImageView.loadImage(@IdRes drawable: Int?) {
    drawable?.let {
        Glide.with(this.context)
                .load(drawable)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(this)
    }
}
