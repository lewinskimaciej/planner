package com.atc.planner.data.repository.places_repository.data_source.firebase_database

import com.atc.planner.data.model.local.Beacon
import com.atc.planner.data.model.local.Place
import com.atc.planner.data.repository.places_repository.SightsFilterDetails
import io.reactivex.Completable
import io.reactivex.Single


interface FirebaseDatabaseDataSource {
    fun savePlace(place: Place): Completable
    fun removePlace(place: Place): Completable
    fun getPlaces(city: String, filterDetails: SightsFilterDetails? = null): Single<List<Place>>
    fun getBeaconsNearby(city: String): Single<List<Beacon>>
    fun getPlaceByAreaId(areaId: String?): Single<Place>
}