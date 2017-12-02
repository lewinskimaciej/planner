package com.atc.planner.data.model.remote

import com.atc.planner.data.model.BaseModel
import com.google.gson.annotations.SerializedName

data class Route(@SerializedName("overview_polyline") var overviewPolyline: OverviewPolyline)
    : BaseModel()