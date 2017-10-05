package com.atc.planner.data.models.remote.places_api

import com.atc.planner.data.models.remote.common.LatLongLocation
import com.google.gson.annotations.SerializedName

data class Geometry(@SerializedName("location") var location: LatLongLocation)
