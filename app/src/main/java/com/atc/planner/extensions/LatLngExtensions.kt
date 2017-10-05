package com.atc.planner.extensions

import com.google.android.gms.maps.model.LatLng
import com.sygic.travel.sdk.model.geo.Bounds

fun LatLng.boundingBox(radius: Int): Bounds? {
    val earthRadius = 6371.toDouble()
    val radiusInKm = radius / 1000

    val x1 = this.longitude - Math.toDegrees(radiusInKm / earthRadius / Math.cos(Math.toRadians(this.latitude)))
    val x2 = this.longitude + Math.toDegrees(radiusInKm / earthRadius / Math.cos(Math.toRadians(this.latitude)))
    val y1 = this.latitude + Math.toDegrees(radiusInKm / earthRadius)
    val y2 = this.latitude - Math.toDegrees(radiusInKm / earthRadius)

    val bounds = Bounds()
    bounds.east = x1.toFloat()
    bounds.west = x2.toFloat()
    bounds.north = y1.toFloat()
    bounds.south = y2.toFloat()

    return bounds
}