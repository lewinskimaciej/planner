package com.atc.planner.data.models.remote

import com.atc.planner.data.models.BaseModel
import com.google.gson.annotations.SerializedName

data class DirectionsResponse(@SerializedName("routes") var routes: List<Route>)
    : BaseModel()