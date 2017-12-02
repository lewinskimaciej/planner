package com.atc.planner.data.models.local

import com.atc.planner.data.models.remote.sygic_api.Category

enum class PlaceCategory {
    SIGHT, FOOD, AMUSEMENT, ART_GALLERY, MUSEUM, PHYSICAL_ACTIVITY, VIEW, OTHER
}

fun Category?.asPlaceCategory(): PlaceCategory = when (this) {
    Category.EATING -> PlaceCategory.FOOD

    Category.DISCOVERING,
    Category.GOING_OUT,
    Category.PLAYING,
    Category.RELAXING -> PlaceCategory.AMUSEMENT

    Category.HIKING -> PlaceCategory.VIEW

    Category.DOING_SPORTS -> PlaceCategory.PHYSICAL_ACTIVITY

    Category.SIGHTSEEING -> PlaceCategory.SIGHT

    else -> PlaceCategory.OTHER
}