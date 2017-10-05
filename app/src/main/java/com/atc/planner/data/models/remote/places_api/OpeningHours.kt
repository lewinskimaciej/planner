package com.atc.planner.data.models.remote.places_api

import com.google.gson.annotations.SerializedName

data class OpeningHours(@SerializedName("open_now") var openNow: Boolean,
                        @SerializedName("weekday_text")var weekdayText: List<String?>?)
