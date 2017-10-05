package com.atc.planner.data.repository.places_nearby_repository.data_source.places_api

import com.atc.planner.R
import com.atc.planner.commons.StringProvider
import com.atc.planner.data.models.remote.places_api.NearbyPlacesEnvelope
import com.atc.planner.data.models.remote.places_api.PlaceDetails
import com.atc.planner.data.models.remote.places_api.RankBy
import com.atc.planner.data.models.remote.places_api.Type
import com.atc.planner.data.remote_service.PlacesNearbyRemoteService
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Single
import javax.inject.Inject

class PlacesApiDataSourceImpl @Inject constructor(private val placesNearbyRemoteService: PlacesNearbyRemoteService,
                                                  private val stringProvider: StringProvider)
    : PlacesApiDataSource {
    override fun getPlaces(latLng: LatLng, radius: Int, rankBy: RankBy?, type: Type?): Single<NearbyPlacesEnvelope<PlaceDetails>> {
        val key = stringProvider.getString(R.string.places_api_key)
        val formattedLocation = "${latLng.latitude},${latLng.longitude}"

        return placesNearbyRemoteService.getNearbyPlaces(key, formattedLocation, radius, rankBy?.value, type?.value)
    }

    override fun getPlacesByToken(nextPageToken: String?): Single<NearbyPlacesEnvelope<PlaceDetails>> {
        val key = stringProvider.getString(R.string.places_api_key)

        return placesNearbyRemoteService.getNextPageOfNearbyPlaces(key, nextPageToken)
    }
}
