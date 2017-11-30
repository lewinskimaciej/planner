package com.atc.planner.data.models.local

import com.google.gson.annotations.SerializedName

data class Beacon(@SerializedName("uuid") var uuid: String? = null,
                  @SerializedName("major") var major: String? = null,
                  @SerializedName("minor") var minor: String? = null,
                  @SerializedName("place") var place: Place? = null)