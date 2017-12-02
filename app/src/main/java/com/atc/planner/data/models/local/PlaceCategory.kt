package com.atc.planner.data.models.local

import com.atc.planner.data.models.remote.sygic_api.Category

enum class PlaceCategory {
    SIGHT, FOOD, AMUSEMENT, ART_GALLERY, MUSEUM, PHYSICAL_ACTIVITY, OTHER
}

fun Category?.asPlaceType(): PlaceCategory = when (this) {
    Category.EATING -> PlaceCategory.FOOD

    Category.DISCOVERING,
    Category.GOING_OUT,
    Category.PLAYING,
    Category.RELAXING -> PlaceCategory.AMUSEMENT

    Category.SIGHTSEEING -> PlaceCategory.SIGHT

    else -> PlaceCategory.OTHER
}