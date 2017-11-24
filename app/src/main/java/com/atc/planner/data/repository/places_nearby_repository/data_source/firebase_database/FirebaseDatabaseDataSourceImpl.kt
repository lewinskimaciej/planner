package com.atc.planner.data.repository.places_nearby_repository.data_source.firebase_database

import com.atc.planner.data.models.local.BeaconPlace
import com.atc.planner.data.models.local.LocalPlace
import com.atc.planner.data.repository.places_nearby_repository.SightsFilterDetails
import com.atc.planner.extensions.orZero
import com.github.ajalt.timberkt.d
import com.github.ajalt.timberkt.e
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class FirebaseDatabaseDataSourceImpl @Inject constructor(private val firebaseFirestore: FirebaseFirestore,
                                                         private val gson: Gson)
    : FirebaseDatabaseDataSource {
    override fun savePlace(localPlace: LocalPlace): Completable = Completable.create { emitter ->
        firebaseFirestore.collection("places")
                .add(localPlace)
                .addOnSuccessListener { emitter.onComplete() }
                .addOnFailureListener { emitter.onError(it) }
    }

    override fun removePlace(localPlace: LocalPlace): Completable {
        return Completable.create { emitter ->
            firebaseFirestore.collection("places")
                    .document(localPlace.remoteId.orEmpty())
                    .delete()
                    .addOnSuccessListener { emitter.onComplete() }
                    .addOnFailureListener { emitter.onError(it) }

        }
    }

    override fun getPlaces(city: String, filterDetails: SightsFilterDetails?): Single<List<LocalPlace>> = Observable.create<LocalPlace> { emitter ->
        d { "getPlaces" }
        firebaseFirestore.collection("places")
                .get()
                .addOnSuccessListener {
                    d { "getPlaces onSuccess" }
                    it.forEach {
                        val place = gson.fromJson<LocalPlace>(gson.toJson(it.data), LocalPlace::class.java)
                        emitter.onNext(place)
                        d { "parsed ${place.remoteId}" }
                    }
                    emitter.onComplete()
                }
                .addOnFailureListener {
                    e(it)
                    emitter.onError(it)
                }
    }.filter {
        if (it.targetsChildren == true) {
            filterDetails?.targetsChildren == true
        } else {
            true
        }
    }.filter {
        it.entryFee.orZero() <= filterDetails?.maxEntryFee.orZero()
    }.filter {
        !((filterDetails?.canBeAMuseum == false && it.isMuseum == true)
                || (filterDetails?.canBeAnArtGallery == false && it.isArtGallery == true)
                || (filterDetails?.canBePhysicalActivity == false && it.isPhysicalActivity == true)
                || (filterDetails?.canBeOutdoors == false && it.isOutdoors == true))
    }
            .toList()

    override fun getBeaconsNearby(city: String): Single<List<BeaconPlace>> = Observable.create<BeaconPlace> { emitter ->
        firebaseFirestore.collection("beacons")
                .get()
                .addOnSuccessListener {
                    it.forEach {
                        val json = gson.toJson(it.data)
                        val place = gson.fromJson<BeaconPlace>(json, BeaconPlace::class.java)
                        emitter.onNext(place)
                    }
                    emitter.onComplete()
                }
                .addOnFailureListener {
                    emitter.onError(it)
                }
    }.toList()
}