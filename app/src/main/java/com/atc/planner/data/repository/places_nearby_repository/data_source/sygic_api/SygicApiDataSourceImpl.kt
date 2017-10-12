package com.atc.planner.data.repository.places_nearby_repository.data_source.sygic_api

import android.content.res.Resources
import com.atc.planner.data.models.remote.sygic_api.Category
import com.atc.planner.extensions.boundingBox
import com.github.ajalt.timberkt.d
import com.google.android.gms.maps.model.LatLng
import com.sygic.travel.sdk.StSDK
import com.sygic.travel.sdk.api.Callback
import com.sygic.travel.sdk.model.place.Place
import com.sygic.travel.sdk.model.query.PlacesQuery
import io.reactivex.Single
import javax.inject.Inject

class SygicApiDataSourceImpl @Inject constructor(private val stSDK: StSDK)
    : SygicApiDataSource {

    override fun getPlaces(latLng: LatLng, radius: Int, categories: List<Category>)
            : Single<List<Place>> = Single.create<List<Place>> {

        val placesQuery = PlacesQuery()
        placesQuery.bounds = latLng.boundingBox(radius)
        placesQuery.categories = categories.map { it.value }
        placesQuery.limit = 100

        stSDK.getPlaces(placesQuery, object : Callback<List<Place?>?>() {
            override fun onSuccess(data: List<Place?>?) {
                d { "getPlaces: onSuccess" }
                if (data != null) {
                    it.onSuccess(data.filterNotNull())
                } else {
                    it.onError(Resources.NotFoundException("Returned list was NULL"))
                }
            }

            override fun onFailure(t: Throwable) {
                d { "getPlaces: onFailure" }
                it.onError(t)
            }
        })
    }
}
