package com.atc.planner.data.repository.places_repository

import com.atc.planner.data.model.local.Beacon
import com.atc.planner.data.model.local.Place
import com.atc.planner.data.model.remote.DirectionsResponse
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Single

interface PlacesRepository {
    fun getPlacesNearby(latLng: LatLng, filterDetails: SightsFilterDetails? = null): Single<List<Place>>
    fun getBeaconsNearby(latLng: LatLng): Single<List<Beacon>>
    fun getDirections(source: LatLng, dest: LatLng): Single<DirectionsResponse>
    fun getLocallySavedPlacesNearby(): List<Place>
    fun getRoute(currentLocation: LatLng, filterDetails: SightsFilterDetails?): Single<ArrayList<Place?>>
    fun getPlaceByAreaId(areaId: String?): Single<Place>
}
