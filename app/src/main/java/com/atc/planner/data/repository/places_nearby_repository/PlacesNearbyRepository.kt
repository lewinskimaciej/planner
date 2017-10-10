package com.atc.planner.data.repository.places_nearby_repository

import com.atc.planner.data.models.local.LocalPlace
import com.atc.planner.data.models.remote.places_api.NearbyPlacesEnvelope
import com.atc.planner.data.models.remote.places_api.PlaceDetails
import com.google.android.gms.maps.model.LatLng
import com.sygic.travel.sdk.model.place.Place
import io.reactivex.Single

interface PlacesNearbyRepository {
    // TODO: return local model instead of remote one
    fun getRestaurantsNearby(latLng: LatLng, radius: Int): Single<NearbyPlacesEnvelope<PlaceDetails>>
    fun getRestaurantsNearby(nextPageToken: String?): Single<NearbyPlacesEnvelope<PlaceDetails>>

    fun getSightsNearby(latLng: LatLng, radius: Int): Single<List<LocalPlace>>
}
