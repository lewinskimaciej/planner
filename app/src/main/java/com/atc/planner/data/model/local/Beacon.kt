package com.atc.planner.data.model.local

import com.google.gson.annotations.SerializedName

data class Beacon(@SerializedName("id") var id: String? = null,
                  @SerializedName("uuid") var uuid: String? = null,
                  @SerializedName("major") var major: String? = null,
                  @SerializedName("minor") var minor: String? = null,
                  @SerializedName("areaId") var areaId: String? = null) {

    override fun equals(other: Any?): Boolean = other is Beacon && this.id == other.id

    override fun hashCode(): Int = this.id.orEmpty().hashCode()
}