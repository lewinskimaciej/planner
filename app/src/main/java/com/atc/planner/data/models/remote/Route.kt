package com.atc.planner.data.models.remote

import com.atc.planner.data.models.BaseModel
import com.google.gson.annotations.SerializedName

data class Route(@SerializedName("overview_polyline") var overviewPolyline: OverviewPolyline)
    : BaseModel()