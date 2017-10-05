package com.atc.planner.di.modules

import com.atc.planner.data.repository.places_nearby_repository.PlacesNearbyRepository
import com.atc.planner.data.repository.places_nearby_repository.PlacesNearbyRepositoryImpl
import com.atc.planner.data.repository.places_nearby_repository.data_source.places_api.PlacesApiDataSource
import com.atc.planner.data.repository.places_nearby_repository.data_source.places_api.PlacesApiDataSourceImpl
import com.atc.planner.data.repository.places_nearby_repository.data_source.sygic_api.SygicApiDataSource
import com.atc.planner.data.repository.places_nearby_repository.data_source.sygic_api.SygicApiDataSourceImpl
import dagger.Binds
import dagger.Module

@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindPlacesApiDataSource(placesApiDataSource: PlacesApiDataSourceImpl): PlacesApiDataSource

    @Binds
    abstract fun bindSygicApiDataSource(sygicApiDataSource: SygicApiDataSourceImpl): SygicApiDataSource

    @Binds
    abstract fun  bindPlacesNearbyRepository(placesNearbyRepository: PlacesNearbyRepositoryImpl): PlacesNearbyRepository
}
