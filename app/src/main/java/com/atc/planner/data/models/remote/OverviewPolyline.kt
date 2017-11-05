package com.atc.planner.data.models.remote

import com.atc.planner.data.models.BaseModel
import com.google.gson.annotations.SerializedName

data class OverviewPolyline(@SerializedName("points") var points: String)
    : BaseModel()