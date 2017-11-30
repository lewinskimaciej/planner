package com.atc.planner.data.repository.places_nearby_repository

import com.atc.planner.R
import com.atc.planner.commons.CityProvider
import com.atc.planner.commons.StringProvider
import com.atc.planner.data.models.local.BeaconPlace
import com.atc.planner.data.models.local.DataSource
import com.atc.planner.data.models.local.LocalPlace
import com.atc.planner.data.models.local.asPlaceType
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

    private var places: List<LocalPlace> = listOf()

    private var beaconsNearby: List<BeaconPlace> = listOf()
    private var lastBeaconsDownloadTime: DateTime? = null
    private var lastCityBeaconsWereDownloadedFor: String? = null

    override fun getLocallySavedSightsNearby(): List<LocalPlace> = places

    override fun getSightsNearby(latLng: LatLng, filterDetails: SightsFilterDetails?): Single<List<LocalPlace>> {
        val city = cityProvider.getCity(latLng)

        return if (city != null) {
            firebaseDatabaseDataSource.getPlaces(city, filterDetails)
                    .doOnSuccess {
                        d { "size BEFORE: ${it.size}" }
                        places = it
                    }
//                    .toObservable()
//                    .flatMapIterable { it }
//                    .map { localPlace ->
//                        val originalPlace = localPlace.copy()
//                        localPlace.targetsChildren = (0..10).random() == 0
//                        if ((0..3).random() == 0) {
//                            localPlace.entryFee = (0..21).random().toFloat()
//                        }
//                        localPlace.childrenFriendly = (0..3).random() < 3
//                        localPlace.hasSouvenirs = (0..3).random() == 0
//                        localPlace.hasView = (0..3).random() == 0
//
//                        val placeType = (0..4).random()
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
//                        firebaseDatabaseDataSource.savePlace(localPlace).subscribe({ d { "saved ${localPlace.name}" } }, ::e)
//
//                        localPlace
//                    }
//                    .toList()
//                    .doOnSuccess { d { "size AFTER: ${it.size}" } }
        } else {
            Single.error(NoSuchElementException("City for these coordinates was not found"))
        }
    }

    override fun getPlacesFromSygic(latLng: LatLng): Single<List<LocalPlace>> {
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

                    val localPlace = LocalPlace(it.id,
                            it.level,
                            "Wrocław",
                            it.categories?.map { it.asCategory().asPlaceType() },
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

    override fun getRoad(currentLocation: LatLng, filterDetails: SightsFilterDetails?): Single<ArrayList<LocalPlace?>> = Single.create { emitter ->
        val placesToChooseFrom = ArrayList(places)
        val chosenPlaces: ArrayList<LocalPlace?> = arrayListOf()

        var highestRatedPlace: LocalPlace? = null  // point of origin
        var highestRatedPlaceWeight = 0f
        for (place in places) {
            val placeWeight = place.attractiveness(currentLocation, filterDetails, true, ArrayList(places))
            if (placeWeight > highestRatedPlaceWeight) {
                highestRatedPlace = place
                highestRatedPlaceWeight = placeWeight
            }
        }
        chosenPlaces.add(highestRatedPlace)
        placesToChooseFrom.remove(highestRatedPlace)
        var lastBestPlace = highestRatedPlace
        for (count in 0..5) {
            val tempPlace = lastBestPlace?.getHighestRatedClosePlace(filterDetails, placesToChooseFrom)
            lastBestPlace = tempPlace
            if (tempPlace != null) {
                chosenPlaces.add(tempPlace)
                placesToChooseFrom.remove(tempPlace)
                d { "CHOSE place #$count: ${tempPlace.name}" }
            }
        }

        emitter.onSuccess(chosenPlaces)
    }

    private fun LocalPlace.getHighestRatedClosePlace(filterDetails: SightsFilterDetails?, placesToChooseFrom: ArrayList<LocalPlace>): LocalPlace? {
        var highestRatedPlace: LocalPlace? = null  // point of origin
        var highestRatedPlaceWeight = 0f
        for (place in placesToChooseFrom) {
            val placeWeight = place.attractiveness(this.location.asLatLng(), filterDetails, true, placesToChooseFrom)
            if (placeWeight > highestRatedPlaceWeight) {
                highestRatedPlace = place
                highestRatedPlaceWeight = placeWeight
            }
        }
        return highestRatedPlace
    }

    private fun LocalPlace.attractiveness(currentLocation: LatLng, filterDetails: SightsFilterDetails?, countClosest: Boolean, placesToChooseFrom: ArrayList<LocalPlace>): Float {
        var attractiveness: Float = this.rating * 2

        if (filterDetails?.hasSouvenirs == true) {
            attractiveness += 1
        }
        if (filterDetails?.childrenFriendly == true) {
            attractiveness += 1
        }
        if (countClosest) {
            this.getClosestPlaces(3).forEach { attractiveness += it.attractiveness(this.location.asLatLng(), filterDetails, false, placesToChooseFrom) * 0.5f }
        }

        val maxDistance = placesToChooseFrom.maximumDistanceFrom(this)
        val distance = currentLocation.asLocation().distanceTo(this.location.asLatLng().asLocation())

        attractiveness += maxDistance - distance

        d { "${this.name} -> attractiveness: $attractiveness, distance: $distance, maxDistance: $maxDistance" }

        return attractiveness
    }

    private fun LocalPlace.getClosestPlaces(amount: Int): List<LocalPlace> = places.map { Pair(it.location.asLatLng().asLocation().distanceTo(location.asLatLng().asLocation()), it) }
            .sortedBy { it.first }
            .take(amount)
            .map { it.second }

    private fun List<LocalPlace>?.maximumDistanceFrom(localPlace: LocalPlace): Float {
        var highestDistance = 0f
        this?.forEach {
            val distance = localPlace.location?.asLatLng().asLocation().distanceTo(it.location?.asLatLng().asLocation())
            if (distance > highestDistance) {
                highestDistance = distance
            }
        }
        return highestDistance
    }

}
