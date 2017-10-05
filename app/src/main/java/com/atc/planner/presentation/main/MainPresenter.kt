package com.atc.planner.presentation.main

import com.atc.planner.R
import com.atc.planner.commons.LocationProvider
import com.atc.planner.commons.StringProvider
import com.atc.planner.data.models.remote.nearby_places.RankBy
import com.atc.planner.data.models.remote.nearby_places.Type
import com.atc.planner.data.repository.places_nearby_repository.PlacesNearbyRepository
import com.atc.planner.di.scopes.ActivityScope
import com.atc.planner.extensions.asLatLng
import com.atc.planner.presentation.base.BasePresenter
import com.atc.planner.presentation.main.adapter.PlaceItem
import com.atc.planner.presentation.main.adapter.PlaceItemModel
import com.github.ajalt.timberkt.d
import com.github.ajalt.timberkt.e
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.Serializable
import javax.inject.Inject

@ActivityScope
class MainPresenter @Inject constructor(private val stringProvider: StringProvider,
                                        private val locationProvider: LocationProvider,
                                        private val placesNearbyRepository: PlacesNearbyRepository)
    : BasePresenter<MainView>() {

    private var currentLocation: LatLng? = null
    private var nextPage: String? = null

    override fun onViewCreated(data: Serializable?) {
        view?.askForLocationPermission()
    }

    fun onPermissionsGranted() {
        d { "onPermissionsGranted" }
        locationProvider.getLastLocation({
            currentLocation = it?.asLatLng()

            currentLocation?.let {
                placesNearbyRepository.getNearbyPlaces(it, 10000, RankBy.PROMINENCE, Type.RESTAURANT)
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.computation())
                        .toObservable()
                        .doOnNext { nextPage = it.nextPageToken }
                        .map { it.results.orEmpty() }
                        .flatMapIterable { list -> list }
                        .map { PlaceItemModel(it.placeId, it.name, it.vicinity, it.photos?.get(0)) }
                        .map { PlaceItem(it) }
                        .toList()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            view?.setItems(it)
                        }, ::e)
            }
        }, {
            e(it)
        })
    }

    fun onPermissionsRefused() {
        d { "onPermissionsRefused" }
        view?.showAlertDialog(stringProvider.getString(R.string.location_permission_refused_dialog_title),
                stringProvider.getString(R.string.location_permission_refused_dialog_content))
        view?.askForLocationPermission()
    }

    fun onLoadMore() {
        if (nextPage != null) {
            placesNearbyRepository.getNextPageOfNearbyPlaces(nextPage)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.computation())
                    .toObservable()
                    .doOnNext { nextPage = it.nextPageToken }
                    .map { it.results.orEmpty() }
                    .flatMapIterable { list -> list }
                    .map { PlaceItemModel(it.placeId, it.name, it.vicinity, it.photos?.get(0)) }
                    .map { PlaceItem(it) }
                    .toList()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        view?.addItems(it)
                    }, ::e)
        }
    }

    fun noMoreLoad() {
        e { "noMoreLoad" }
    }
}
