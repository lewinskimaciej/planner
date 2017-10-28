package com.atc.planner.data.models.local

import com.google.gson.annotations.SerializedName

data class BeaconPlace(@SerializedName("uuid") var uuid: String? = null,
                       @SerializedName("major") var major: String? = null,
                       @SerializedName("minor") var minor: String? = null,
                       @SerializedName("localPlace") var localPlace: LocalPlace? = null)