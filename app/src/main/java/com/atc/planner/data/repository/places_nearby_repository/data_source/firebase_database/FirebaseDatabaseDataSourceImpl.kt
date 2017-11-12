package com.atc.planner.data.repository.places_nearby_repository.data_source.firebase_database

import com.atc.planner.data.models.local.BeaconPlace
import com.atc.planner.data.models.local.LocalPlace
import com.atc.planner.data.repository.places_nearby_repository.SightsFilterDetails
import com.atc.planner.extensions.orMax
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

    override fun getPlaces(city: String, filterDetails: SightsFilterDetails?): Single<List<LocalPlace>> = Observable.create<LocalPlace> { emitter ->
        val query = firebaseFirestore.collection("places")
        if (filterDetails != null) {
            query.whereLessThan("entryFee", filterDetails.maxEntryFee.orMax())
//                .whereEqualTo("childrenFriendly", filterDetails.childrenFriendly)

            if (filterDetails.canBeAMuseum == false) {
                query.whereEqualTo("isMuseum", false)
            }
            if (filterDetails.canBeAnArtGallery == false) {
                query.whereEqualTo("isArtGallery", false)
            }
            if (filterDetails.canBePhysicalActivity == false) {
                query.whereEqualTo("isPhysicalActivity", false)
            }
            if (filterDetails.canBeOutdoors == false) {
                query.whereEqualTo("isOutdoors", false)
            }
        }

        query.get()
                .addOnSuccessListener {
                    it.forEach {
                        val json = gson.toJson(it.data)
                        val place = gson.fromJson<LocalPlace>(json, LocalPlace::class.java)
                        emitter.onNext(place)
                    }
                    emitter.onComplete()
                }
                .addOnFailureListener {
                    emitter.onError(it)
                }
    }.toList()

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