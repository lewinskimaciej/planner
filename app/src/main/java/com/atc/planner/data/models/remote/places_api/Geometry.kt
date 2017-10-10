package com.atc.planner.data.models.remote.places_api

import com.google.gson.annotations.SerializedName

data class Geometry(@SerializedName("location") var location: LatLongLocation)
