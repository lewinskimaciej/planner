package com.atc.planner.data.repository.places_repository

import com.atc.planner.R
import com.atc.planner.commons.CityProvider
import com.atc.planner.commons.StringProvider
import com.atc.planner.data.model.local.Beacon
import com.atc.planner.data.model.local.Place
import com.atc.planner.data.remote_service.DirectionsRemoteService
import com.atc.planner.data.repository.places_repository.data_source.firebase_database.FirebaseDatabaseDataSource
import com.atc.planner.extension.asLatLng
import com.atc.planner.extension.asLocation
import com.github.ajalt.timberkt.d
import com.github.ajalt.timberkt.e
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlacesRepositoryImpl @Inject constructor(private val firebaseDatabaseDataSource: FirebaseDatabaseDataSource,
                                               private val directionsRemoteService: DirectionsRemoteService,
                                               private val cityProvider: CityProvider,
                                               private val stringProvider: StringProvider)
    : PlacesRepository {

    private var places: List<Place> = listOf()

    private var beaconsNearby: List<Beacon> = listOf()

    override fun getLocallySavedPlacesNearby(): List<Place> = places

    override fun getPlacesNearby(latLng: LatLng, filterDetails: SightsFilterDetails?): Single<List<Place>> {
        val city = cityProvider.getCity(latLng)

        return if (city != null) {
            firebaseDatabaseDataSource.getPlaces(city, filterDetails)
                    .doOnSuccess {
                        places = it
                    }
        } else {
            Single.error(NoSuchElementException("City for these coordinates was not found"))
        }
    }

    override fun getPlaceByAreaId(areaId: String?): Single<Place> =
            firebaseDatabaseDataSource.getPlaceByAreaId(areaId)

    override fun getBeaconsNearby(latLng: LatLng): Single<List<Beacon>> {
        if (beaconsNearby.isNotEmpty()) {
            return Single.just(beaconsNearby)
        }

        val city = cityProvider.getCity(latLng)

        return if (city != null) {
            firebaseDatabaseDataSource.getBeaconsNearby(city)
                    .doOnSuccess {
                        beaconsNearby = it
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

    override fun getRoute(currentLocation: LatLng, filterDetails: SightsFilterDetails?): Single<ArrayList<Place?>> = Single.create { emitter ->
        d { "getRoute" }
        val placesToChooseFrom = ArrayList(places)
        val chosenPlaces: ArrayList<Place?> = arrayListOf()

        var closestPlace: Place? = null
        var closestPlaceDistance = Float.MAX_VALUE
        for (index in places.indices) {
            val distance = currentLocation.asLocation().distanceTo(places[index].location.asLatLng().asLocation())
            if (distance < closestPlaceDistance) {
                closestPlace = places[index]
                closestPlaceDistance = distance
            }
        }

        chosenPlaces.add(closestPlace)
        placesToChooseFrom.remove(closestPlace)
        var lastBestPlace = closestPlace
        for (count in 0..10) {
            val tempPlace = lastBestPlace?.getHighestRatedClosePlace(filterDetails, placesToChooseFrom)
            lastBestPlace = tempPlace
            if (tempPlace != null) {
                chosenPlaces.add(tempPlace)
                placesToChooseFrom.remove(tempPlace)
            }
        }

        emitter.onSuccess(chosenPlaces)
    }

    private fun Place.getHighestRatedClosePlace(filterDetails: SightsFilterDetails?, placesToChooseFrom: ArrayList<Place>): Place? {
        var highestRatedPlace: Place? = null  // point of origin
        var highestRatedPlaceWeight = 0f
        for (place in placesToChooseFrom) {
            val placeWeight = place.attractiveness(this.location.asLatLng(), filterDetails, placesToChooseFrom)
            if (placeWeight > highestRatedPlaceWeight) {
                highestRatedPlace = place
                highestRatedPlaceWeight = placeWeight
            }
        }
        return highestRatedPlace
    }

    private fun Place.attractiveness(currentLocation: LatLng, filterDetails: SightsFilterDetails?, placesToChooseFrom: ArrayList<Place>): Float {
        val maxDistance = placesToChooseFrom.maximumDistanceFrom(this)
        val distance = this.location.asLatLng().asLocation().distanceTo(currentLocation.asLocation())

        var attractiveness: Float = (maxDistance - distance) / 10

        attractiveness += this.rating * 3

        if (filterDetails?.hasSouvenirs == true) {
            attractiveness += 5
        }
        if (filterDetails?.childrenFriendly == true) {
            attractiveness += 5
        }

        return attractiveness
    }

    private fun List<Place>?.maximumDistanceFrom(place: Place): Float {
        var highestDistance = 0f
        if (this != null) {
            for (index in this.indices) {
                val distance = place.location?.asLatLng().asLocation().distanceTo(this[index].location?.asLatLng().asLocation())
                if (distance > highestDistance) {
                    highestDistance = distance
                }
            }
        }
        return highestDistance
    }
}
