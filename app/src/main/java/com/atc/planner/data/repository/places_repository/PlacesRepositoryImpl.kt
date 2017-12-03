package com.atc.planner.data.repository.places_repository

import com.atc.planner.R
import com.atc.planner.commons.CityProvider
import com.atc.planner.commons.StringProvider
import com.atc.planner.data.model.local.Beacon
import com.atc.planner.data.model.local.DataSource
import com.atc.planner.data.model.local.Place
import com.atc.planner.data.model.local.asPlaceCategory
import com.atc.planner.data.model.remote.sygic_api.Category
import com.atc.planner.data.model.remote.sygic_api.asCategory
import com.atc.planner.data.remote_service.DirectionsRemoteService
import com.atc.planner.data.repository.places_repository.data_source.firebase_database.FirebaseDatabaseDataSource
import com.atc.planner.data.repository.places_repository.data_source.sygic_api.SygicApiDataSource
import com.atc.planner.extension.asLatLng
import com.atc.planner.extension.asLatLong
import com.atc.planner.extension.asLocation
import com.atc.planner.extension.random
import com.github.ajalt.timberkt.d
import com.github.ajalt.timberkt.e
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlacesRepositoryImpl @Inject constructor(private val firebaseDatabaseDataSource: FirebaseDatabaseDataSource,
                                               private val directionsRemoteService: DirectionsRemoteService,
                                               private val sygicApiDataSource: SygicApiDataSource,
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
                        d { "size BEFORE: ${it.size}" }
                        places = it
                    }
        } else {
            Single.error(NoSuchElementException("City for these coordinates was not found"))
        }
    }

    override fun getPlacesFromSygic(latLng: LatLng): Single<List<Place>> {
        return sygicApiDataSource.getPlaces(latLng, 10000, listOf(Category.SIGHTSEEING, Category.EATING, Category.DISCOVERING))
                .toObservable()
                .flatMapIterable { it }
                .map { it.id.orEmpty() }
                .toList()
                .flatMap { sygicApiDataSource.getPlacesDetailed(it) }
                .toObservable()
                .flatMapIterable { it }
                .map {
                    val description = if (it.detail != null) {
                        it.detail?.description?.text
                    } else {
                        if (!it.perex.isNullOrEmpty()) {
                            it.perex
                        } else {
                            it.nameSuffix
                        }
                    }

                    val photos = it.detail
                            ?.mainMedia
                            ?.media
                            .orEmpty()
                            .filter { "photo" == it.type }
                            .map { it.url }

                    val localPlace = Place(it.id,
                            it.level,
                            "Wrocław",
                            it.categories?.map { it.asCategory().asPlaceCategory() },
                            it.rating,
                            it.location.asLatLong(),
                            it.name,
                            it.nameSuffix,
                            description,
                            it.url,
                            it.thumbnailUrl,
                            it.detail?.openingHours,
                            photos,
                            DataSource.SYGIC)

                    localPlace.targetsChildren = (0..10).random() == 0
                    if ((0..3).random() == 0) {
                        localPlace.entryFee = (0..21).random().toFloat()
                    }
                    localPlace.childrenFriendly = (0..3).random() < 3
                    localPlace.hasSouvenirs = (0..3).random() == 0
                    localPlace.hasView = (0..3).random() == 0

                    val placeType = (0..4).random()
                    localPlace.isArtGallery = false
                    localPlace.isMuseum = false
                    localPlace.isOutdoors = false
                    localPlace.isPhysicalActivity = false
                    when (placeType) {
                        0 -> {
                            localPlace.isArtGallery = true
                            localPlace.isOutdoors = (0..20).random() == 0
                        }
                        1 -> localPlace.isMuseum = true
                        2 -> localPlace.isOutdoors = true
                        3 -> {
                            localPlace.isPhysicalActivity = true
                            localPlace.isOutdoors = (0..1).random() == 0
                        }
                    }
                    localPlace.rating = ((0..100).random().toFloat() / 10f)

                    localPlace
                }
                .filter { it.photos.orEmpty().isNotEmpty() && !it.description.isNullOrEmpty() }
                .doOnNext { place ->
                    firebaseDatabaseDataSource.savePlace(place).subscribe({ d { "saved ${place.name}" } }, ::e)
                }
                .toList()
    }

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

        var closestPlace: Place? = null  // point of origin
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
            attractiveness += 10
        }
        if (filterDetails?.childrenFriendly == true) {
            attractiveness += 10
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
