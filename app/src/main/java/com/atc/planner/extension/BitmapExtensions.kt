package com.atc.planner.extension

import android.graphics.Bitmap

fun Bitmap?.resize(width: Int, height: Int): Bitmap = Bitmap.createScaledBitmap(this, width, height, false)
