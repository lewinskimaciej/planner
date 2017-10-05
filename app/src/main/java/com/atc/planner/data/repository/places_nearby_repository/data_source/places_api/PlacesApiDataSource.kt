package com.atc.planner.data.repository.places_nearby_repository.data_source.places_api

import com.atc.planner.data.models.remote.places_api.NearbyPlacesEnvelope
import com.atc.planner.data.models.remote.places_api.PlaceDetails
import com.atc.planner.data.models.remote.places_api.RankBy
import com.atc.planner.data.models.remote.places_api.Type
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Single

interface PlacesApiDataSource {
    fun getPlaces(latLng: LatLng, radius: Int, rankBy: RankBy?, type: Type?): Single<NearbyPlacesEnvelope<PlaceDetails>>
    fun getPlacesByToken(nextPageToken: String?): Single<NearbyPlacesEnvelope<PlaceDetails>>
}
