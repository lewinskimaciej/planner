package com.atc.planner.extensions

import android.content.res.Resources
import android.util.TypedValue
import java.util.*

fun Int.dpToPx() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), Resources.getSystem().displayMetrics)

fun Int.pxToDp() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, this.toFloat(), Resources.getSystem().displayMetrics)

fun Int.pxToSp() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this.toFloat(), Resources.getSystem().displayMetrics)

fun Float.dpToPx() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics)

fun Float.pxToDp() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, this, Resources.getSystem().displayMetrics)

fun Float.pxToSp() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this, Resources.getSystem().displayMetrics)

fun Int?.orZero(): Int = this ?: 0

fun Long?.orZero(): Long = this ?: 0

fun Float?.orZero(): Float = this ?: 0.0f

fun Double?.orZero(): Double = this ?: 0.0

fun Long?.orMax(): Long = this ?: Long.MAX_VALUE

fun Float?.orMax(): Float = this ?: Float.MAX_VALUE

fun Double?.orMax(): Double = this ?: Double.MAX_VALUE

fun ClosedRange<Int>.random() = Random().nextInt(endInclusive + 1 - start) + start