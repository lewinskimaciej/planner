package com.atc.planner.data.repository.places_nearby_repository

import com.atc.planner.R
import com.atc.planner.commons.CityProvider
import com.atc.planner.commons.StringProvider
import com.atc.planner.data.models.local.BeaconPlace
import com.atc.planner.data.models.local.LocalPlace
import com.atc.planner.data.remote_services.DirectionsRemoteService
import com.atc.planner.data.repository.places_nearby_repository.data_source.firebase_database.FirebaseDatabaseDataSource
import com.atc.planner.extensions.orZero
import com.github.ajalt.timberkt.e
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Single
import org.joda.time.DateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlacesNearbyRepositoryImpl @Inject constructor(private val firebaseDatabaseDataSource: FirebaseDatabaseDataSource,
                                                     private val directionsRemoteService: DirectionsRemoteService,
                                                     private val cityProvider: CityProvider,
                                                     private val stringProvider: StringProvider)
    : PlacesNearbyRepository {

    var beaconsNearby: List<BeaconPlace> = listOf()
    var lastBeaconsDownloadTime: DateTime? = null
    var lastCityBeaconsWereDownloadedFor: String? = null

    override fun getSightsNearby(latLng: LatLng, filterDetails: SightsFilterDetails?): Single<List<LocalPlace>> {
        val city = cityProvider.getCity(latLng)

        return if (city != null) {
            firebaseDatabaseDataSource.getPlaces(city, filterDetails)
//                    .toObservable()
//                    .flatMapIterable { it }
//                    .map { localPlace ->
//                        localPlace.targetsChildren = (0..10).random() == 0
//                        if ((0..3).random() == 0) {
//                            localPlace.entryFee = (0..20).random().toFloat()
//                        }
//                        localPlace.childrenFriendly = (0..3).random() < 3
//                        firebaseDatabaseDataSource.savePlace(localPlace).subscribe({ d { "updated ${localPlace.name}" } }, ::e)
//                        localPlace.hasSouvenirs = (0..3).random() == 0
//                        localPlace.hasView = (0..3).random() == 0
//
//                        val placeType = (0..3).random()
//                        localPlace.isArtGallery = false
//                        localPlace.isMuseum = false
//                        localPlace.isOutdoors = false
//                        localPlace.isPhysicalActivity = false
//                        when (placeType) {
//                            0 -> {
//                                localPlace.isArtGallery = true
//                                localPlace.isOutdoors = (0..20).random() == 0
//                            }
//                            1 -> localPlace.isMuseum = true
//                            2 -> localPlace.isOutdoors = true
//                            3 -> {
//                                localPlace.isPhysicalActivity = true
//                                localPlace.isOutdoors = (0..1).random() == 0
//                            }
//                        }
//                        localPlace.rating = ((0..100).random().toFloat() / 10f)
//                        firebaseDatabaseDataSource.savePlace(localPlace).subscribe({ d { "updated ${localPlace.name}" } }, ::e)
//                        localPlace
//                    }
//                    .toList()
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

    override fun getDirections(source: LatLng, dest: LatLng) =
            directionsRemoteService.getDirections("${source.latitude},${source.longitude}",
                    "${dest.latitude},${dest.longitude}",
                    stringProvider.getString(R.string.places_api_key))

}
