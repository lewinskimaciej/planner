package com.atc.planner.data.models.local

import com.atc.planner.data.models.BaseModel

data class LocalPlace(
        var id: Long = 0,
        var remoteId: String? = null,
        var level: String? = null,
        var categories: List<PlaceType>? = null,
        var rating: Float = 0.toFloat(),
        var location: LatLong? = null,
        var name: String? = null,
        var address: String? = null,
        var description: String? = null,
        var url: String? = null,
        var thumbnailUrl: String? = null,
        var openingHours: String? = null,
        var photos: List<String?>? = null,
        var source: DataSource? = null
) : BaseModel()