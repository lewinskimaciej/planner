package com.atc.planner.data.repository.places_nearby_repository

import com.atc.planner.data.models.BaseModel

data class SightsFilterDetails(var targetsChildren: Boolean? = false,
                               var childrenFriendly: Boolean? = true,
                               var canBeAMuseum: Boolean? = true,
                               var canBeAnArtGallery: Boolean? = true,
                               var canBePhysicalActivity: Boolean? = true,
                               var canBeOutdoors: Boolean? = true,
                               var hasSouvenirs: Boolean? = true,
                               var hasView: Boolean? = true,
                               var maxEntryFee: Float? = 20f) : BaseModel()