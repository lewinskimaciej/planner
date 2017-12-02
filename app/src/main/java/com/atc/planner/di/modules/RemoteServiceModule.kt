package com.atc.planner.di.modules

import com.atc.planner.data.remote_service.DirectionsRemoteService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class RemoteServiceModule {

    @Singleton
    @Provides
    fun provideDirectionsService(retrofit: Retrofit): DirectionsRemoteService = retrofit.create(DirectionsRemoteService::class.java)
}