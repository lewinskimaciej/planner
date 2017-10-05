package com.atc.planner.data.repository.places_nearby_repository.data_source.sygic_api

import com.atc.planner.data.models.remote.sygic_api.Category
import com.google.android.gms.maps.model.LatLng
import com.sygic.travel.sdk.model.place.Place
import io.reactivex.Single

interface SygicApiDataSource {
    fun getPlaces(latLng: LatLng, radius: Int, categories: List<Category>): Single<List<Place>>
}
