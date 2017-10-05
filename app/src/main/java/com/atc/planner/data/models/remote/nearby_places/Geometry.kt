package com.atc.planner.data.models.remote.nearby_places

import com.atc.planner.data.models.remote.common.LatLongLocation
import com.google.gson.annotations.SerializedName

data class Geometry(@SerializedName("location") var location: LatLongLocation)
