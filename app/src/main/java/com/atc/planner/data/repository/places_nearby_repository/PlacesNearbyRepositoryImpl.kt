package com.atc.planner.data.repository.places_nearby_repository

import com.atc.planner.commons.CityProvider
import com.atc.planner.data.models.local.LocalPlace
import com.atc.planner.data.models.remote.places_api.NearbyPlacesEnvelope
import com.atc.planner.data.models.remote.places_api.PlaceDetails
import com.atc.planner.data.models.remote.places_api.RankBy
import com.atc.planner.data.models.remote.places_api.Type
import com.atc.planner.data.repository.places_nearby_repository.data_source.firebase_database.FirebaseDatabaseDataSource
import com.atc.planner.data.repository.places_nearby_repository.data_source.places_api.PlacesApiDataSource
import com.atc.planner.data.repository.places_nearby_repository.data_source.sygic_api.SygicApiDataSource
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlacesNearbyRepositoryImpl @Inject constructor(private val placesApiDataSource: PlacesApiDataSource,
                                                     private val sygicApiDataSource: SygicApiDataSource,
                                                     private val firebaseDatabaseDataSource: FirebaseDatabaseDataSource,
                                                     private val cityProvider: CityProvider)
    : PlacesNearbyRepository {

    override fun getRestaurantsNearby(latLng: LatLng, radius: Int): Single<NearbyPlacesEnvelope<PlaceDetails>>
            = placesApiDataSource.getPlaces(latLng, radius, RankBy.PROMINENCE, Type.RESTAURANT)

    override fun getRestaurantsNearby(nextPageToken: String?): Single<NearbyPlacesEnvelope<PlaceDetails>>
            = placesApiDataSource.getPlacesByToken(nextPageToken)

    override fun getSightsNearby(latLng: LatLng): Single<List<LocalPlace>> {
        val city = cityProvider.getCity(latLng)
        return if (city != null) {
            firebaseDatabaseDataSource.getPlaces(city)
        } else {
            Single.error(NoSuchElementException("City for these coordinates not found"))
        }
    }
}
