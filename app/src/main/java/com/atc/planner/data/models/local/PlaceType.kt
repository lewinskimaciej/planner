package com.atc.planner.data.models.local

import com.atc.planner.data.models.remote.places_api.Type
import com.atc.planner.data.models.remote.sygic_api.Category

enum class PlaceType {
    SIGHT, FOOD, AMUSEMENT, OTHER
}

fun Category?.asPlaceType(): PlaceType = when (this) {
    Category.EATING -> PlaceType.FOOD

    Category.DISCOVERING,
    Category.GOING_OUT,
    Category.PLAYING,
    Category.RELAXING -> PlaceType.AMUSEMENT

    Category.SIGHTSEEING -> PlaceType.SIGHT

    else -> PlaceType.OTHER
}

fun Type?.asPlaceType(): PlaceType = when(this) {
    Type.BOWLING_ALLEY,
    Type.ZOO,
    Type.NIGHT_CLUB,
    Type.AQUARIUM,
    Type.AMUSEMENT_PARK -> PlaceType.AMUSEMENT

    Type.RESTAURANT -> PlaceType.FOOD

    else -> PlaceType.OTHER
}