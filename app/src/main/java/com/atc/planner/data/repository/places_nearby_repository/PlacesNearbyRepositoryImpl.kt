package com.atc.planner.data.repository.places_nearby_repository

import com.atc.planner.commons.CityProvider
import com.atc.planner.data.models.local.BeaconPlace
import com.atc.planner.data.models.local.LocalPlace
import com.atc.planner.data.repository.places_nearby_repository.data_source.firebase_database.FirebaseDatabaseDataSource
import com.atc.planner.data.repository.places_nearby_repository.data_source.sygic_api.SygicApiDataSource
import com.atc.planner.extensions.orZero
import com.github.ajalt.timberkt.e
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Single
import org.joda.time.DateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlacesNearbyRepositoryImpl @Inject constructor(private val sygicApiDataSource: SygicApiDataSource,
                                                     private val firebaseDatabaseDataSource: FirebaseDatabaseDataSource,
                                                     private val cityProvider: CityProvider)
    : PlacesNearbyRepository {

    var sightsNearby: List<LocalPlace> = listOf()
    var lastSightsDownloadTime: DateTime? = null
    var lastCitySightsWereDownloadedFor: String? = null

    var beaconsNearby: List<BeaconPlace> = listOf()
    var lastBeaconsDownloadTime: DateTime? = null
    var lastCityBeaconsWereDownloadedFor: String? = null

    override fun getSightsNearby(latLng: LatLng): Single<List<LocalPlace>> {
        val city = cityProvider.getCity(latLng)
        // if city is same as before, and last call was recent, return cached response
        if (lastCitySightsWereDownloadedFor == city
                && DateTime.now().millis - lastSightsDownloadTime?.millis.orZero() > 1000 * 60 * 5
                && sightsNearby.isNotEmpty()) {
            return Single.just(sightsNearby)
        }

        return if (city != null) {
            firebaseDatabaseDataSource.getPlaces(city)
                    .doOnSuccess {
                        sightsNearby = it
                        lastCitySightsWereDownloadedFor = city
                        lastSightsDownloadTime = DateTime.now()
                    }
                    .doOnError(::e)
        } else {
            Single.error(NoSuchElementException("City for these coordinates was not found"))
        }
    }

    override fun getBeaconsNearby(latLng: LatLng): Single<List<BeaconPlace>> {
        val city = cityProvider.getCity(latLng)
        // if city is same as before, and last call was recent, return cached response
        if (lastCityBeaconsWereDownloadedFor == city
                && DateTime.now().millis - lastBeaconsDownloadTime?.millis.orZero() > 1000 * 60 * 5
                && beaconsNearby.isNotEmpty()) {
            return Single.just(beaconsNearby)
        }

        return if (city != null) {
            firebaseDatabaseDataSource.getBeaconsNearby(city)
                    .doOnSuccess {
                        beaconsNearby = it
                        lastCityBeaconsWereDownloadedFor = city
                        lastBeaconsDownloadTime = DateTime.now()
                    }
                    .doOnError(::e)
        } else {
            Single.error(NoSuchElementException("City for these coordinates was not found"))
        }
    }
}
