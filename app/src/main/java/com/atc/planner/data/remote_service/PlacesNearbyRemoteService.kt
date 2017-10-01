package com.atc.planner.data.remote_service

import com.atc.planner.data.models.remote.nearby_places.NearbyPlacesEnvelope
import com.atc.planner.data.models.remote.nearby_places.PlaceDetails
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface PlacesNearbyRemoteService {
    @GET("https://maps.googleapis.com/maps/api/place/nearbysearch/json")
    fun getNearbyPlaces(@Query("key") apiKey: String,
                        @Query("location") latLong: String,
                        @Query("radius") radius: Int,
                        @Query("rankby") rankBy: String?,
                        @Query("type") type: String?): Single<NearbyPlacesEnvelope<PlaceDetails>>

}