package com.atc.planner.di.modules

import com.atc.planner.data.repository.places_repository.PlacesRepository
import com.atc.planner.data.repository.places_repository.PlacesRepositoryImpl
import com.atc.planner.data.repository.places_repository.data_source.firebase_database.FirebaseDatabaseDataSource
import com.atc.planner.data.repository.places_repository.data_source.firebase_database.FirebaseDatabaseDataSourceImpl
import com.atc.planner.data.repository.user_details_repository.UserDetailsRepository
import com.atc.planner.data.repository.user_details_repository.UserDetailsRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindFirebaseDatabaseDataSource(firebaseDatabaseDataSource: FirebaseDatabaseDataSourceImpl): FirebaseDatabaseDataSource

    @Binds
    abstract fun bindPlacesNearbyRepository(placesRepository: PlacesRepositoryImpl): PlacesRepository

    @Binds
    abstract fun bindUserDetailsRepository(userDetailsRepository: UserDetailsRepositoryImpl): UserDetailsRepository
}
