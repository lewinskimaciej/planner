package com.atc.planner.extension

import com.atc.planner.data.model.local.LatLong
import com.google.android.gms.maps.model.LatLng

fun LatLong?.asLatLng(): LatLng = LatLng(this?.lat.orZero(), this?.long.orZero())

fun LatLng?.toLatLong(): LatLong = LatLong(this?.latitude, this?.longitude)

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