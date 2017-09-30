package com.atc.planner.commons

import android.content.Context
import android.support.v4.content.ContextCompat
import javax.inject.Inject

interface ColorProvider {
    fun getColor(id: Int): Int?
}

class ColorProviderImpl @Inject constructor(val context: Context) : ColorProvider {
    override fun getColor(id: Int): Int? = ContextCompat.getColor(context, id)
}
