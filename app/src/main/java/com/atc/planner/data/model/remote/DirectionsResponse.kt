package com.atc.planner.data.model.remote

import com.atc.planner.data.model.BaseModel
import com.google.gson.annotations.SerializedName

data class DirectionsResponse(@SerializedName("routes") var routes: List<Route>)
    : BaseModel()