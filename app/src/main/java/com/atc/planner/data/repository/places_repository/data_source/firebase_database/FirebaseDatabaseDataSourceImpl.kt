package com.atc.planner.data.repository.places_repository.data_source.firebase_database

import com.atc.planner.data.model.local.Beacon
import com.atc.planner.data.model.local.Place
import com.atc.planner.data.model.local.PlaceCategory
import com.atc.planner.data.repository.places_repository.SightsFilterDetails
import com.atc.planner.extension.orZero
import com.github.ajalt.timberkt.d
import com.github.ajalt.timberkt.e
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class FirebaseDatabaseDataSourceImpl @Inject constructor(private val firebaseFirestore: FirebaseFirestore,
                                                         private val gson: Gson)
    : FirebaseDatabaseDataSource {
    override fun savePlace(place: Place): Completable = Completable.create { emitter ->
        firebaseFirestore.collection("places")
                .add(place)
                .addOnSuccessListener { emitter.onComplete() }
                .addOnFailureListener { emitter.onError(it) }
    }

    override fun removePlace(place: Place): Completable {
        return Completable.create { emitter ->
            firebaseFirestore.collection("places")
                    .document(place.remoteId.orEmpty())
                    .delete()
                    .addOnSuccessListener { emitter.onComplete() }
                    .addOnFailureListener { emitter.onError(it) }

        }
    }

    override fun getPlaces(city: String, filterDetails: SightsFilterDetails?): Single<List<Place>> = Observable.create<Place> { emitter ->
        firebaseFirestore.collection("places")
                .whereEqualTo("city", city)
                .get()
                .addOnSuccessListener {
                    it.forEach {
                        val place = gson.fromJson<Place>(gson.toJson(it.data), Place::class.java)
                        emitter.onNext(place)
                        d { "parsed ${place.remoteId}" }
                    }
                    emitter.onComplete()
                }
                .addOnFailureListener {
                    e(it)
                    emitter.onError(it)
                }
    }.observeOn(Schedulers.computation())
            .map {
                it.isMuseum = it.categories?.contains(PlaceCategory.MUSEUM)
                it.isArtGallery = it.categories?.contains(PlaceCategory.ART_GALLERY)
                it.isPhysicalActivity = it.categories?.contains(PlaceCategory.PHYSICAL_ACTIVITY)
                it
            }
            .filter {
                if (it.targetsChildren == true) {
                    filterDetails?.targetsChildren == true
                } else {
                    true
                }
            }
            .filter {
                it.entryFee.orZero() <= filterDetails?.maxEntryFee.orZero()
            }
            .filter {
                !((filterDetails?.canBeAMuseum == false && it.isMuseum == true)
                        || (filterDetails?.canBeAnArtGallery == false && it.isArtGallery == true)
                        || (filterDetails?.canBePhysicalActivity == false && it.isPhysicalActivity == true)
                        || (filterDetails?.canBeOutdoors == false && it.isOutdoors == true))
            }
            .toList()

    override fun getBeaconsNearby(city: String): Single<List<Beacon>> = Observable.create<Beacon> { emitter ->
        firebaseFirestore.collection("beacons")
                .get()
                .addOnSuccessListener {
                    it.forEach {
                        val json = gson.toJson(it.data)
                        val place = gson.fromJson<Beacon>(json, Beacon::class.java)
                        emitter.onNext(place)
                    }
                    emitter.onComplete()
                }
                .addOnFailureListener {
                    emitter.onError(it)
                }
    }.toList()
}