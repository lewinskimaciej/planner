package com.atc.planner.data.repository.places_nearby_repository

import com.atc.planner.R
import com.atc.planner.commons.CityProvider
import com.atc.planner.commons.StringProvider
import com.atc.planner.data.models.local.Beacon
import com.atc.planner.data.models.local.DataSource
import com.atc.planner.data.models.local.Place
import com.atc.planner.data.models.local.asPlaceCategory
import com.atc.planner.data.models.remote.sygic_api.Category
import com.atc.planner.data.models.remote.sygic_api.asCategory
import com.atc.planner.data.remote_services.DirectionsRemoteService
import com.atc.planner.data.repository.places_nearby_repository.data_source.firebase_database.FirebaseDatabaseDataSource
import com.atc.planner.data.repository.places_nearby_repository.data_source.sygic_api.SygicApiDataSource
import com.atc.planner.extensions.*
import com.github.ajalt.timberkt.d
import com.github.ajalt.timberkt.e
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Single
import org.joda.time.DateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlacesNearbyRepositoryImpl @Inject constructor(private val firebaseDatabaseDataSource: FirebaseDatabaseDataSource,
                                                     private val directionsRemoteService: DirectionsRemoteService,
                                                     private val sygicApiDataSource: SygicApiDataSource,
                                                     private val cityProvider: CityProvider,
                                                     private val stringProvider: StringProvider)
    : PlacesNearbyRepository {

    private var places: List<Place> = listOf()

    private var beaconsNearby: List<Beacon> = listOf()
    private var lastBeaconsDownloadTime: DateTime? = null
    private var lastCityBeaconsWereDownloadedFor: String? = null

    override fun getLocallySavedSightsNearby(): List<Place> = places

    override fun getSightsNearby(latLng: LatLng, filterDetails: SightsFilterDetails?): Single<List<Place>> {
        val city = cityProvider.getCity(latLng)

        return if (city != null) {
            firebaseDatabaseDataSource.getPlaces(city, filterDetails)
                    .doOnSuccess {
                        d { "size BEFORE: ${it.size}" }
                        places = it
                    }
//                    .toObservable()
//                    .flatMapIterable { it }
//                    .map { place ->
//                        val originalPlace = place.copy()
//                        place.targetsChildren = (0..10).random() == 0
//                        if ((0..3).random() == 0) {
//                            place.entryFee = (0..21).random().toFloat()
//                        }
//                        place.childrenFriendly = (0..3).random() < 3
//                        place.hasSouvenirs = (0..3).random() == 0
//                        place.hasView = (0..3).random() == 0
//
//                        val placeType = (0..4).random()
//                        place.isArtGallery = false
//                        place.isMuseum = false
//                        place.isOutdoors = false
//                        place.isPhysicalActivity = false
//                        when (placeType) {
//                            0 -> {
//                                place.isArtGallery = true
//                                place.isOutdoors = (0..20).random() == 0
//                            }
//                            1 -> place.isMuseum = true
//                            2 -> place.isOutdoors = true
//                            3 -> {
//                                place.isPhysicalActivity = true
//                                place.isOutdoors = (0..1).random() == 0
//                            }
//                        }
//                        place.rating = ((0..100).random().toFloat() / 10f)
//                        firebaseDatabaseDataSource.savePlace(place).subscribe({ d { "saved ${place.name}" } }, ::e)
//
//                        place
//                    }
//                    .toList()
//                    .doOnSuccess { d { "size AFTER: ${it.size}" } }
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

    override fun getRoute(currentLocation: LatLng, filterDetails: SightsFilterDetails?): Single<ArrayList<Place?>> = Single.create { emitter ->
        d { "getRoute" }
        val placesToChooseFrom = ArrayList(places)
        val chosenPlaces: ArrayList<Place?> = arrayListOf()

        var closesPlace: Place? = null  // point of origin
        var closestPlaceDistance = Float.MAX_VALUE
        for (index in places.indices) {
            val distance = currentLocation.asLocation().distanceTo(places[index].location.asLatLng().asLocation())
            if (distance < closestPlaceDistance) {
                closesPlace = places[index]
                closestPlaceDistance = distance
            }
        }
        d { "getRoute first chosen" }
        chosenPlaces.add(closesPlace)
        placesToChooseFrom.remove(closesPlace)
        var lastBestPlace = closesPlace
        for (count in 0..10) {
            d { "getRoute choosing $count" }
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
//        d { "CHOSE ------------------------------ choosing best place for ${this.name}" }
        for (place in placesToChooseFrom) {
            val placeWeight = place.attractiveness(this.location.asLatLng(), filterDetails, true, placesToChooseFrom)
//            d { "CHOSE ${place.name} has weight of $placeWeight" }
            if (placeWeight > highestRatedPlaceWeight) {
                highestRatedPlace = place
                highestRatedPlaceWeight = placeWeight
            }
        }
//        d { "CHOSE chosen -------> ${highestRatedPlace?.name} attractiveness: $highestRatedPlaceWeight" }
        return highestRatedPlace
    }

    private fun Place.attractiveness(currentLocation: LatLng, filterDetails: SightsFilterDetails?, countClosest: Boolean, placesToChooseFrom: ArrayList<Place>): Float {
        var attractiveness: Float = this.rating * 2

        if (filterDetails?.hasSouvenirs == true) {
            attractiveness += 1
        }
        if (filterDetails?.childrenFriendly == true) {
            attractiveness += 1
        }

        val maxDistance = placesToChooseFrom.maximumDistanceFrom(this)
        val distance = this.location.asLatLng().asLocation().distanceTo(currentLocation.asLocation())

        attractiveness += (maxDistance - distance) / 10

//        d {"${this.name} attr: $attractiveness distance: $distance maxDistance: $maxDistance"}

        return attractiveness
    }

    private fun Place.getClosestPlaces(amount: Int): List<Place> = places.map { Pair(it.location.asLatLng().asLocation().distanceTo(location.asLatLng().asLocation()), it) }
            .sortedBy { it.first }
            .take(amount)
            .map { it.second }

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
