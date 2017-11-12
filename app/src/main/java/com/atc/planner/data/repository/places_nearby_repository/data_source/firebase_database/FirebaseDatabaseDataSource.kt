package com.atc.planner.data.repository.places_nearby_repository.data_source.firebase_database

import com.atc.planner.data.models.local.BeaconPlace
import com.atc.planner.data.models.local.LocalPlace
import com.atc.planner.data.repository.places_nearby_repository.SightsFilterDetails
import io.reactivex.Completable
import io.reactivex.Single


interface FirebaseDatabaseDataSource {
    fun savePlace(localPlace: LocalPlace): Completable
    fun getPlaces(city: String, filterDetails: SightsFilterDetails? = null): Single<List<LocalPlace>>
    fun getBeaconsNearby(city: String): Single<List<BeaconPlace>>
}