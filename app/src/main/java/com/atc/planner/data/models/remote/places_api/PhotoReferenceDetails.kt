package com.atc.planner.data.models.remote.places_api

import com.google.gson.annotations.SerializedName

data class PhotoReferenceDetails(@SerializedName("height") var height: Int,
                                 @SerializedName("width") var width: Int,
                                 @SerializedName("html_attributions") var htmlAttributions: List<String>?,
                                 @SerializedName("photo_reference") var photoReference: String?)
