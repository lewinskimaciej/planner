package com.atc.planner.data.repository.places_nearby_repository

import com.atc.planner.data.models.local.LocalPlace
import com.atc.planner.data.models.remote.places_api.NearbyPlacesEnvelope
import com.atc.planner.data.models.remote.places_api.PlaceDetails
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Single

interface PlacesNearbyRepository {
    fun getRestaurantsNearby(latLng: LatLng, radius: Int): Single<NearbyPlacesEnvelope<PlaceDetails>>

    fun getRestaurantsNearby(nextPageToken: String?): Single<NearbyPlacesEnvelope<PlaceDetails>>

    fun getSightsNearby(latLng: LatLng): Single<List<LocalPlace>>
}
