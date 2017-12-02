package com.atc.planner.extension

import com.atc.planner.data.model.local.LatLong
import com.google.android.gms.maps.model.LatLng
import com.sygic.travel.sdk.model.geo.Bounds
import com.sygic.travel.sdk.model.geo.Location

fun LatLng.boundingBox(radius: Int): Bounds? {
    val earthRadius = 6371.toDouble()
    val radiusInKm = radius / 1000

    val x1 = this.longitude - Math.toDegrees(radiusInKm / earthRadius / Math.cos(Math.toRadians(this.latitude)))
    val x2 = this.longitude + Math.toDegrees(radiusInKm / earthRadius / Math.cos(Math.toRadians(this.latitude)))
    val y1 = this.latitude + Math.toDegrees(radiusInKm / earthRadius)
    val y2 = this.latitude - Math.toDegrees(radiusInKm / earthRadius)

    val bounds = Bounds()
    bounds.east = x2.toFloat()
    bounds.west = x1.toFloat()
    bounds.north = y1.toFloat()
    bounds.south = y2.toFloat()

    return bounds
}

fun Location?.asLatLong(): LatLong = LatLong(this?.lat.orZero().toDouble(), this?.lng.orZero().toDouble())

fun Location?.asLatLng(): LatLng = LatLng(this?.lat.orZero().toDouble(), this?.lng.orZero().toDouble())

fun LatLong?.asLatLng(): LatLng = LatLng(this?.lat.orZero(), this?.long.orZero())

fun LatLng?.asLatLong(): LatLong = LatLong(this?.latitude, this?.longitude)

fun LatLng?.asLocation(): android.location.Location {
    val location = android.location.Location("${this?.latitude},${this?.longitude}")
    location.latitude = this?.latitude.orZero()
    location.longitude = this?.longitude.orZero()
    return location
}

fun LatLng.distanceTo(other: LatLng): Float = LatLong(this.latitude, this.longitude).distanceTo(LatLong(other.latitude, other.longitude))

fun LatLong.distanceTo(other: LatLong): Float {
    val start = android.location.Location("Starting point")
    start.latitude = this.lat.orZero()
    start.longitude = this.long.orZero()
    val end = android.location.Location("Ending point")
    end.latitude = other.lat.orZero()
    end.longitude = other.long.orZero()

    return start.distanceTo(end)
}