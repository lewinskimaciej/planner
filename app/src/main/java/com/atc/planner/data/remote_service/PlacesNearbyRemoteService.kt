package com.atc.planner.data.remote_service

import com.atc.planner.data.models.remote.places_api.NearbyPlacesEnvelope
import com.atc.planner.data.models.remote.places_api.PlaceDetails
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

    @GET("https://maps.googleapis.com/maps/api/place/nearbysearch/json")
    fun getNextPageOfNearbyPlaces(@Query("key") apiKey: String,
                                  @Query("pagetoken") nextPageToken: String?): Single<NearbyPlacesEnvelope<PlaceDetails>>
}
