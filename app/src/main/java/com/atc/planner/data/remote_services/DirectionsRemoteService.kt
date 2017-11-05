package com.atc.planner.data.remote_services

import com.atc.planner.data.models.remote.DirectionsResponse
import io.reactivex.Single
import retrofit2.http.POST
import retrofit2.http.Query

interface DirectionsRemoteService {
    @POST("maps/api/directions/json?sensor=false&mode=transit")
    fun getDirections(@Query("origin") sourceLatLng: String,
                      @Query("destination") destLatLng: String,
                      @Query("key") apiKey: String): Single<DirectionsResponse>
}