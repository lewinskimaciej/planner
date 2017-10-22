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
import com.atc.planner.extensions.orZero
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Single
import org.joda.time.DateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlacesNearbyRepositoryImpl @Inject constructor(private val placesApiDataSource: PlacesApiDataSource,
                                                     private val sygicApiDataSource: SygicApiDataSource,
                                                     private val firebaseDatabaseDataSource: FirebaseDatabaseDataSource,
                                                     private val cityProvider: CityProvider)
    : PlacesNearbyRepository {

    var sightsNearby: List<LocalPlace> = listOf()
    var lastSightsDownloadTime: DateTime? = null
    var lastCitySightsWereDownloadedFor: String? = null

    override fun getRestaurantsNearby(latLng: LatLng, radius: Int): Single<NearbyPlacesEnvelope<PlaceDetails>>
            = placesApiDataSource.getPlaces(latLng, radius, RankBy.PROMINENCE, Type.RESTAURANT)

    override fun getRestaurantsNearby(nextPageToken: String?): Single<NearbyPlacesEnvelope<PlaceDetails>>
            = placesApiDataSource.getPlacesByToken(nextPageToken)

    override fun getSightsNearby(latLng: LatLng): Single<List<LocalPlace>> {
        val city = cityProvider.getCity(latLng)
        // if city is same as before, and last call was recent, return cached response
        if (lastCitySightsWereDownloadedFor == city
                && DateTime.now().millis - lastSightsDownloadTime?.millis.orZero() > 1000 * 60 * 5
                && sightsNearby.isNotEmpty()) {
            return Single.just(sightsNearby)
        }

        return if (city != null) {
            firebaseDatabaseDataSource.getPlaces(city).doOnSuccess {
                sightsNearby = it
                lastCitySightsWereDownloadedFor = city
                lastSightsDownloadTime = DateTime.now()
            }
        } else {
            Single.error(NoSuchElementException("City for these coordinates not found"))
        }
    }
}
