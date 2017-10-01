package com.atc.planner.di.modules

import com.atc.planner.data.repository.places_nearby_repository.PlacesNearbyRepository
import com.atc.planner.data.repository.places_nearby_repository.PlacesNearbyRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
abstract class RepositoryModule {

    @Binds
    abstract fun  bindPlacesNearbyRepository(placesNearbyRepositoryImpl: PlacesNearbyRepositoryImpl): PlacesNearbyRepository
}