package com.atc.planner.data.models.local

import com.google.gson.annotations.SerializedName

data class BeaconPlace(@SerializedName("uuid") var uuid: String? = null,
                       @SerializedName("localPlace") var localPlace: LocalPlace? = null)