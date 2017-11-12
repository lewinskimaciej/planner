package com.atc.planner.di.modules

import com.atc.planner.data.repository.places_nearby_repository.PlacesNearbyRepository
import com.atc.planner.data.repository.places_nearby_repository.PlacesNearbyRepositoryImpl
import com.atc.planner.data.repository.places_nearby_repository.data_source.firebase_database.FirebaseDatabaseDataSource
import com.atc.planner.data.repository.places_nearby_repository.data_source.firebase_database.FirebaseDatabaseDataSourceImpl
import com.atc.planner.data.repository.places_nearby_repository.data_source.sygic_api.SygicApiDataSource
import com.atc.planner.data.repository.places_nearby_repository.data_source.sygic_api.SygicApiDataSourceImpl
import com.atc.planner.data.repository.user_details_repository.UserDetailsRepository
import com.atc.planner.data.repository.user_details_repository.UserDetailsRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindSygicApiDataSource(sygicApiDataSource: SygicApiDataSourceImpl): SygicApiDataSource

    @Binds
    abstract fun bindFirebaseDatabaseDataSource(firebaseDatabaseDataSource: FirebaseDatabaseDataSourceImpl): FirebaseDatabaseDataSource

    @Binds
    abstract fun bindPlacesNearbyRepository(placesNearbyRepository: PlacesNearbyRepositoryImpl): PlacesNearbyRepository

    @Binds
    abstract fun bindUserDetailsRepository(userDetailsRepository: UserDetailsRepositoryImpl): UserDetailsRepository
}
