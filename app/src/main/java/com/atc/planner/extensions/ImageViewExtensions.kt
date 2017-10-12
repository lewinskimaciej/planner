package com.atc.planner.extensions

import android.net.Uri
import android.support.annotation.IdRes
import android.widget.ImageView
import com.atc.planner.commons.GlideApp
import com.bumptech.glide.load.engine.DiskCacheStrategy
import jp.wasabeef.glide.transformations.BitmapTransformation
import java.io.File

fun ImageView.loadImage(url: String?, bitmapTransformation: BitmapTransformation? = null) {
    url?.let {
        if (bitmapTransformation != null) {
            GlideApp.with(this.context)
                    .asBitmap()
                    .transform(bitmapTransformation)
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(this)
        } else {
            GlideApp.with(this.context)
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(this)
        }
    }
}

fun ImageView.loadImage(url: String?, @IdRes error: Int?) {
    url?.let {
        if (error != null) {
            GlideApp.with(this.context)
                    .load(url)
                    .error(error)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(this)
        }
    }
}

fun ImageView.loadImage(file: File?) {
    file?.let {
        GlideApp.with(this.context)
                .load(file)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(this)
    }
}

fun ImageView.loadImage(file: File?, @IdRes error: Int?) {
    file?.let {
        if (error != null) {
            GlideApp.with(this.context)
                    .load(file)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(error)
                    .into(this)
        }
    }
}

fun ImageView.loadImage(uri: Uri?) {
    uri?.let {
        GlideApp.with(this.context)
                .load(uri)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(this)
    }
}

fun ImageView.loadImage(@IdRes drawable: Int?, @IdRes error: Int?) {
    drawable?.let {
        if (error != null) {
            GlideApp.with(this.context)
                    .load(drawable)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(error)
                    .into(this)
        }
    }
}

fun ImageView.loadImage(@IdRes drawable: Int?) {
    drawable?.let {
        GlideApp.with(this.context)
                .load(drawable)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(this)
    }
}
