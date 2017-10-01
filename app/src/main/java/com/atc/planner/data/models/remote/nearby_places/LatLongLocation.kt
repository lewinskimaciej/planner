package com.atc.planner.data.models.remote.nearby_places

import com.google.gson.annotations.SerializedName

data class LatLongLocation(@SerializedName("lat") var lat: Double,
                           @SerializedName("lng") var long: Double)