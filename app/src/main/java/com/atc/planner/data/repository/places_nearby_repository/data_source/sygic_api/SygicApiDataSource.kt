package com.atc.planner.data.repository.places_nearby_repository.data_source.sygic_api

import com.atc.planner.data.model.remote.sygic_api.Category
import com.google.android.gms.maps.model.LatLng
import com.sygic.travel.sdk.model.place.Place
import io.reactivex.Single

interface SygicApiDataSource {
    fun getPlaces(latLng: LatLng, radius: Int, categories: List<Category>): Single<List<Place>>
    fun getPlaceDetailed(id: String?): Single<Place>
    fun getPlacesDetailed(ids: List<String>): Single<List<Place?>>
}
