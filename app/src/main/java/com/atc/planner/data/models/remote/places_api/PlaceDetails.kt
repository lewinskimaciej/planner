package com.atc.planner.data.models.remote.places_api

import com.google.gson.annotations.SerializedName

data class PlaceDetails(@SerializedName("geometry") var geometry: Geometry?,
                        @SerializedName("icon") var icon: String?,
                        @SerializedName("name") var name: String?,
                        @SerializedName("opening_hours") var openingHours: OpeningHours?,
                        @SerializedName("photos") var photos: List<PhotoReferenceDetails>?,
                        @SerializedName("place_id") var placeId: String?,
                        @SerializedName("rating") var rating: Float?,
                        @SerializedName("reference") var reference: String?,
                        @SerializedName("types") var types: List<Type>?,
                        @SerializedName("vicinity") var vicinity: String?)
