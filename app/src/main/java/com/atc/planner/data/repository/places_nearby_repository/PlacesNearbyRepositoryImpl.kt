package com.atc.planner.data.repository.places_nearby_repository

import com.atc.planner.data.models.remote.places_api.NearbyPlacesEnvelope
import com.atc.planner.data.models.remote.places_api.PlaceDetails
import com.atc.planner.data.models.remote.places_api.RankBy
import com.atc.planner.data.models.remote.places_api.Type
import com.atc.planner.data.models.remote.sygic_api.Category
import com.atc.planner.data.repository.places_nearby_repository.data_source.places_api.PlacesApiDataSource
import com.atc.planner.data.repository.places_nearby_repository.data_source.sygic_api.SygicApiDataSource
import com.google.android.gms.maps.model.LatLng
import com.sygic.travel.sdk.StSDK
import com.sygic.travel.sdk.api.Callback
import com.sygic.travel.sdk.model.place.Place
import com.sygic.travel.sdk.model.query.PlacesQuery
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlacesNearbyRepositoryImpl @Inject constructor(private val placesApiDataSource: PlacesApiDataSource,
                                                     private val sygicApiDataSource: SygicApiDataSource)
    : PlacesNearbyRepository {

    override fun getRestaurantsNearby(latLng: LatLng, radius: Int): Single<NearbyPlacesEnvelope<PlaceDetails>>
            = placesApiDataSource.getPlaces(latLng, radius, RankBy.PROMINENCE, Type.RESTAURANT)

    override fun getRestaurantsNearby(nextPageToken: String?): Single<NearbyPlacesEnvelope<PlaceDetails>>
            = placesApiDataSource.getPlacesByToken(nextPageToken)

    override fun getSightsNearby(latLng: LatLng, radius: Int) {
        sygicApiDataSource.getPlaces(latLng, radius, listOf(Category.SIGHTSEEING))
    }
}
