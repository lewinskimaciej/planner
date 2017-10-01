package com.atc.planner.data.models.remote.nearby_places

import com.google.gson.annotations.SerializedName

data class OpeningHours(@SerializedName("open_now") var openNow: Boolean,
                        @SerializedName("weekday_text")var weekdayText: List<String?>?)