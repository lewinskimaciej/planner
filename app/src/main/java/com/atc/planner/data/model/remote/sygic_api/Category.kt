package com.atc.planner.data.model.remote.sygic_api

enum class Category(val value: String) {
    DISCOVERING("discovering"),
    EATING("eating"),
    GOING_OUT("going_out"),
    HIKING("hiking"),
    PLAYING("playing"),
    RELAXING("relaxing"),
    SHOPPING("shopping"),
    SIGHTSEEING("sightseeing"),
    SLEEPING("sleeping"),
    DOING_SPORTS("doing_sports"),
    TRAVELING("traveling"),
    OTHER("other")
}

fun String?.asCategory(): Category = when (this) {
    Category.DISCOVERING.value -> Category.DISCOVERING
    Category.EATING.value -> Category.EATING
    Category.GOING_OUT.value -> Category.GOING_OUT
    Category.HIKING.value -> Category.HIKING
    Category.PLAYING.value -> Category.PLAYING
    Category.RELAXING.value -> Category.RELAXING
    Category.SHOPPING.value -> Category.SHOPPING
    Category.SIGHTSEEING.value -> Category.SIGHTSEEING
    Category.SLEEPING.value -> Category.SLEEPING
    Category.DOING_SPORTS.value -> Category.DOING_SPORTS
    Category.TRAVELING.value -> Category.TRAVELING
    else -> Category.OTHER
}

