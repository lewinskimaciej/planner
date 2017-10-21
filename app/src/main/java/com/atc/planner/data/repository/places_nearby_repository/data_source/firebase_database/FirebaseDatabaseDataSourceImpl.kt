package com.atc.planner.data.repository.places_nearby_repository.data_source.firebase_database

import com.atc.planner.data.models.local.LocalPlace
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import io.reactivex.*
import javax.inject.Inject

class FirebaseDatabaseDataSourceImpl @Inject constructor(private val firebaseFirestore: FirebaseFirestore,
                                                         private val gson: Gson)
    : FirebaseDatabaseDataSource {
    override fun savePlace(localPlace: LocalPlace): Completable = Completable.create {
        val emitter: CompletableEmitter = it
        firebaseFirestore.collection("places")
                .add(localPlace)
                .addOnSuccessListener { emitter.onComplete() }
                .addOnFailureListener { emitter.onError(it) }
    }

    override fun getPlaces(city: String): Single<List<LocalPlace>> = Observable.create<LocalPlace> {
        val emitter: ObservableEmitter<LocalPlace> = it

        firebaseFirestore.collection("places")
                .get()
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
}