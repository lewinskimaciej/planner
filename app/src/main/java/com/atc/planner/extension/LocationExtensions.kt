package com.atc.planner.extension

import android.location.Location
import com.google.android.gms.maps.model.LatLng

fun Location.toLatLong() = LatLng(this.latitude, this.longitude)