package com.atc.planner.di.modules

import com.atc.planner.data.remote_service.PlacesNearbyRemoteService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class RemoteServiceModule {

    @Provides
    fun placesNearbyRemoteService(retrofit: Retrofit): PlacesNearbyRemoteService = retrofit.create(PlacesNearbyRemoteService::class.java)
}