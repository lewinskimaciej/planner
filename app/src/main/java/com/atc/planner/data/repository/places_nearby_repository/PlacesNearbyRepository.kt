package com.atc.planner.data.repository.places_nearby_repository

import com.atc.planner.data.models.local.Beacon
import com.atc.planner.data.models.local.Place
import com.atc.planner.data.models.remote.DirectionsResponse
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Single

interface PlacesNearbyRepository {
    fun getSightsNearby(latLng: LatLng, filterDetails: SightsFilterDetails? = null): Single<List<Place>>
    fun getBeaconsNearby(latLng: LatLng): Single<List<Beacon>>
    fun getDirections(source: LatLng, dest: LatLng): Single<DirectionsResponse>
    fun getPlacesFromSygic(latLng: LatLng): Single<List<Place>>
    fun getLocallySavedSightsNearby(): List<Place>
    fun getRoute(currentLocation: LatLng, filterDetails: SightsFilterDetails?): Single<ArrayList<Place?>>
}
