package com.atc.planner.data.repository.places_nearby_repository

import com.atc.planner.R
import com.atc.planner.commons.StringProvider
import com.atc.planner.data.models.remote.nearby_places.RankBy
import com.atc.planner.data.models.remote.nearby_places.Type
import com.atc.planner.data.remote_service.PlacesNearbyRemoteService
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlacesNearbyRepositoryImpl @Inject constructor(private val placesNearbyRemoteService: PlacesNearbyRemoteService,
                                                     private val stringProvider: StringProvider): PlacesNearbyRepository {
    override fun getNearbyPlaces(latLng: LatLng, radius: Int, rankBy: RankBy?, type: Type?): Single<String> {
        val key = stringProvider.getString(R.string.places_api_key)
        val formattedLocation = "${latLng.latitude},${latLng.longitude}"

        return placesNearbyRemoteService.getNearbyPlaces(key, formattedLocation, radius, rankBy?.value, type?.value)
    }
}