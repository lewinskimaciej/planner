package com.atc.planner.data.models.remote.places_api

import com.google.gson.annotations.SerializedName

data class LatLongLocation(@SerializedName("lat") var lat: Double,
                           @SerializedName("lng") var long: Double)
