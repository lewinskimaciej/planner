package com.atc.planner.data.repository.places_nearby_repository

import com.atc.planner.data.models.remote.nearby_places.RankBy
import com.atc.planner.data.models.remote.nearby_places.Type
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Single

interface PlacesNearbyRepository {
    fun getNearbyPlaces(latLng: LatLng, radius: Int, rankBy: RankBy?, type: Type?): Single<String>
}