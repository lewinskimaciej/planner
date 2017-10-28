package com.atc.planner.data.repository.places_nearby_repository

import com.atc.planner.data.models.local.BeaconPlace
import com.atc.planner.data.models.local.LocalPlace
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Single

interface PlacesNearbyRepository {
    fun getSightsNearby(latLng: LatLng): Single<List<LocalPlace>>
    fun getBeaconsNearby(latLng: LatLng): Single<List<BeaconPlace>>
}
