package com.atc.planner.data.models.remote.places_api

import com.google.gson.annotations.SerializedName

data class NearbyPlacesEnvelope<T>(@SerializedName("results") var results: List<T?>?,
                                   @SerializedName("status") var status: String?,
                                   @SerializedName("next_page_token") var nextPageToken: String?)