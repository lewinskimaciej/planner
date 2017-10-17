package com.atc.planner.data.repository.places_nearby_repository

import com.atc.planner.data.models.local.DataSource
import com.atc.planner.data.models.local.LocalPlace
import com.atc.planner.data.models.local.asPlaceType
import com.atc.planner.data.models.remote.places_api.NearbyPlacesEnvelope
import com.atc.planner.data.models.remote.places_api.PlaceDetails
import com.atc.planner.data.models.remote.places_api.RankBy
import com.atc.planner.data.models.remote.places_api.Type
import com.atc.planner.data.models.remote.sygic_api.Category
import com.atc.planner.data.models.remote.sygic_api.asCategory
import com.atc.planner.data.repository.places_nearby_repository.data_source.places_api.PlacesApiDataSource
import com.atc.planner.data.repository.places_nearby_repository.data_source.sygic_api.SygicApiDataSource
import com.atc.planner.extensions.asLatLong
import com.google.android.gms.maps.model.LatLng
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

    override fun getSightsNearby(latLng: LatLng, radius: Int): Single<List<LocalPlace>>
            = sygicApiDataSource.getPlaces(latLng, radius, listOf(Category.SIGHTSEEING))
            .toObservable()
            .flatMapIterable { it }
            .filter { it.id != null }
            .map { it.id as String }
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

                LocalPlace(0,
                        it.id,
                        it.level,
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
            }
            .toList()

}
