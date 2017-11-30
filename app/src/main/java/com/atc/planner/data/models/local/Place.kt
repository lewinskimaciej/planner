package com.atc.planner.data.models.local

import com.atc.planner.data.models.BaseModel

data class Place(
        var remoteId: String? = null,
        var level: String? = null,
        val city: String? = null,
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
        var source: DataSource? = null,

        var targetsChildren: Boolean? = false,
        var childrenFriendly: Boolean? = true,
        var isMuseum: Boolean? = false,
        var isArtGallery: Boolean? = false,
        var isPhysicalActivity: Boolean? = false,
        var isOutdoors: Boolean? = false,
        var hasSouvenirs: Boolean? = true,
        var hasView: Boolean? = false,
        var entryFee: Float? = 0f
) : BaseModel()