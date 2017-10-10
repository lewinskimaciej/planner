package com.atc.planner.data.models.remote.places_api

import com.google.gson.annotations.SerializedName

enum class Type(val value: String) {
    @SerializedName("accounting")
    ACCOUNTING("accounting"),
    @SerializedName("airport")
    AIRPORT("airport"),
    @SerializedName("amusement_park")
    AMUSEMENT_PARK("amusement_park"),
    @SerializedName("aquarium")
    AQUARIUM("aquarium"),
    @SerializedName("art_gallery")
    ART_GALLERY("art_gallery"),
    @SerializedName("atm")
    ATM("atm"),
    @SerializedName("bakery")
    BAKERY("bakery"),
    @SerializedName("bank")
    BANK("bank"),
    @SerializedName("bar")
    BAR("bar"),
    @SerializedName("beauty_salon")
    BEAUTY_SALON("beauty_salon"),
    @SerializedName("bicycle_store")
    BICYCLE_STORE("bicycle_store"),
    @SerializedName("book_store")
    BOOK_STORE("book_store"),
    @SerializedName("bowling_alley")
    BOWLING_ALLEY("bowling_alley"),
    @SerializedName("bus_station")
    BUS_STATION("bus_station"),
    @SerializedName("cafe")
    CAFE("cafe"),
    @SerializedName("campground")
    CAMPGROUND("campground"),
    @SerializedName("car_dealer")
    CAR_DEALER("car_dealer"),
    @SerializedName("car_rental")
    CAR_RENTAL("car_rental"),
    @SerializedName("car_repair")
    CAR_REPAIR("car_repair"),
    @SerializedName("car_wash")
    CAR_WASH("car_wash"),
    @SerializedName("casino")
    CASINO("casino"),
    @SerializedName("cemetery")
    CEMETERY("cemetery"),
    @SerializedName("church")
    CHURCH("church"),
    @SerializedName("city_hall")
    CITY_HALL("city_hall"),
    @SerializedName("clothing_store")
    CLOTHING_STORE("clothing_store"),
    @SerializedName("convenience_store")
    CONVENIENCE_STORE("convenience_store"),
    @SerializedName("courthouse")
    COURTHOUSE("courthouse"),
    @SerializedName("dentist")
    DENTIST("dentist"),
    @SerializedName("department_store")
    DEPARTMENT_STORE("department_store"),
    @SerializedName("doctor")
    DOCTOR("doctor"),
    @SerializedName("electrician")
    ELECTRICIAN("electrician"),
    @SerializedName("electronics_store")
    ELECTRONICS_STORE("electronics_store"),
    @SerializedName("embassy")
    EMBASSY("embassy"),
    @SerializedName("fire_station")
    FIRE_STATION("fire_station"),
    @SerializedName("florist")
    FLORIST("florist"),
    @SerializedName("funeral_home")
    FUNERAL_HOME("funeral_home"),
    @SerializedName("furniture_store")
    FURNITURE_STORE("furniture_store"),
    @SerializedName("gas_station")
    GAS_STATION("gas_station"),
    @SerializedName("gym")
    GYM("gym"),
    @SerializedName("hair_care")
    HAIR_CARE("hair_care"),
    @SerializedName("hardware_store")
    HARDWARE_STORE("hardware_store"),
    @SerializedName("hindu_temple")
    HINDU_TEMPLE("hindu_temple"),
    @SerializedName("home_goods_store")
    HOME_GOODS_STORE("home_goods_store"),
    @SerializedName("hospital")
    HOSPITAL("hospital"),
    @SerializedName("insurance_agency")
    INSURANCE_AGENCY("insurance_agency"),
    @SerializedName("jewelry_store")
    JEWELRY_STORE("jewelry_store"),
    @SerializedName("laundry")
    LAUNDRY("laundry"),
    @SerializedName("lawyer")
    LAWYER("lawyer"),
    @SerializedName("library")
    LIBRARY("library"),
    @SerializedName("liquor_store")
    LIQUOR_STORE("liquor_store"),
    @SerializedName("local_government_office")
    LOCAL_GOVERNMENT_OFFICE("local_government_office"),
    @SerializedName("locksmith")
    LOCKSMITH("locksmith"),
    @SerializedName("lodging")
    LODGING("lodging"),
    @SerializedName("meal_delivery")
    MEAL_DELIVERY("meal_delivery"),
    @SerializedName("meal_takeaway")
    MEAL_TAKEAWAY("meal_takeaway"),
    @SerializedName("mosque")
    MOSQUE("mosque"),
    @SerializedName("movie_rental")
    MOVIE_RENTAL("movie_rental"),
    @SerializedName("movie_theater")
    MOVIE_THEATER("movie_theater"),
    @SerializedName("moving_company")
    MOVING_COMPANY("moving_company"),
    @SerializedName("museum")
    MUSEUM("museum"),
    @SerializedName("night_club")
    NIGHT_CLUB("night_club"),
    @SerializedName("painter")
    PAINTER("painter"),
    @SerializedName("park")
    PARK("park"),
    @SerializedName("parking")
    PARKING("parking"),
    @SerializedName("pet_store")
    PET_STORE("pet_store"),
    @SerializedName("pharmacy")
    PHARMACY("pharmacy"),
    @SerializedName("physiotherapist")
    PHYSIOTHERAPIST("physiotherapist"),
    @SerializedName("plumber")
    PLUMBER("plumber"),
    @SerializedName("police")
    POLICE("police"),
    @SerializedName("post_office")
    POST_OFFICE("post_office"),
    @SerializedName("real_estate_agency")
    REAL_ESTATE_AGENCY("real_estate_agency"),
    @SerializedName("restaurant")
    RESTAURANT("restaurant"),
    @SerializedName("roofing_contractor")
    ROOFING_CONTRACTOR("roofing_contractor"),
    @SerializedName("rv_park")
    RV_PARK("rv_park"),
    @SerializedName("school")
    SCHOOL("school"),
    @SerializedName("shoe_store")
    SHOE_STORE("shoe_store"),
    @SerializedName("shopping_mall")
    SHOPPING_MALL("shopping_mall"),
    @SerializedName("spa")
    SPA("spa"),
    @SerializedName("stadium")
    STADIUM("stadium"),
    @SerializedName("storage")
    STORAGE("storage"),
    @SerializedName("store")
    STORE("store"),
    @SerializedName("subway_station")
    SUBWAY_STATION("subway_station"),
    @SerializedName("synagogue")
    SYNAGOGUE("synagogue"),
    @SerializedName("taxi_stand")
    TAXI_STAND("taxi_stand"),
    @SerializedName("train_station")
    TRAIN_STATION("train_station"),
    @SerializedName("transit_station")
    TRANSIT_STATION("transit_station"),
    @SerializedName("travel_agency")
    TRAVEL_AGENCY("travel_agency"),
    @SerializedName("university")
    UNIVERSITY("university"),
    @SerializedName("veterinary_care")
    VETERINARY_CARE("veterinary_care"),
    @SerializedName("zoo")
    ZOO("zoo")
}
