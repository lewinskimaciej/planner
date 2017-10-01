package com.atc.planner.data.models.remote.nearby_places

import com.google.gson.annotations.SerializedName

data class Geometry(@SerializedName("location") var location: LatLongLocation)