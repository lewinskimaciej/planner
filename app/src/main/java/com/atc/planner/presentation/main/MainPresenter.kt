package com.atc.planner.presentation.main

import com.atc.planner.R
import com.atc.planner.commons.LocationProvider
import com.atc.planner.commons.StringProvider
import com.atc.planner.data.model.local.Place
import com.atc.planner.data.repository.places_repository.PlacesRepository
import com.atc.planner.data.repository.places_repository.SightsFilterDetails
import com.atc.planner.data.repository.user_details_repository.UserDetailsRepository
import com.atc.planner.di.scopes.ActivityScope
import com.atc.planner.extension.asLatLng
import com.atc.planner.extension.asLatLong
import com.atc.planner.extension.orZero
import com.atc.planner.presentation.base.BaseMvpPresenter
import com.github.ajalt.timberkt.d
import com.github.ajalt.timberkt.e
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.Serializable
import javax.inject.Inject

@ActivityScope
class MainPresenter @Inject constructor(private val stringProvider: StringProvider,
                                        private val locationProvider: LocationProvider,
                                        private val placesRepository: PlacesRepository,
                                        private val userDetailsRepository: UserDetailsRepository)
    : BaseMvpPresenter<MainView>() {

    private var currentLocation: LatLng? = null
    private var lastRefreshDate: Long? = null
    private var count: Int = 0

    override fun onViewCreated(data: Serializable?) {
        view?.askForLocationPermission()
    }

    fun onPermissionsGranted() {
        d { "onPermissionsGranted" }
        locationProvider.getLastLocation({
            currentLocation = it?.asLatLong()

            getPlacesNearby()

        }, {
            e(it)
        })
        locationProvider.startService()
    }

    private fun getPlacesNearby() {
        if (lastRefreshDate == null || System.currentTimeMillis() - lastRefreshDate.orZero() > 2000) {
            currentLocation?.let { location ->
                val filterDetails = userDetailsRepository.getFilterDetails()

                d { "getPlacesNearby" }
                placesRepository.getPlacesNearby(location, filterDetails)
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            d { "got ${it.size} items" }
                            view?.setItems(it)
                            resolveRoute()
                        }, {
                            if (it is NoSuchElementException) {
                                view?.showAlertDialog(stringProvider.getString(R.string.geocoder_encountered_a_problem),
                                        stringProvider.getString(R.string.try_rebooting_your_device))
                            } else {
                                view?.showErrorToast()
                            }
                        })

                lastRefreshDate = System.currentTimeMillis()
            }
        }
    }

    private fun resolveRoute() {
        val existingRoute = userDetailsRepository.getRoute()
        if (existingRoute.isNotEmpty()) {
            getRoute(Single.just(existingRoute))
        } else {
            currentLocation?.let { location ->
                val filterDetails = userDetailsRepository.getFilterDetails()
                getRoute(computeRoute(location, filterDetails))
            }
        }
    }

    private fun getRoute(source: Single<List<Place?>>) {
        source.map { locations ->
            val triples: ArrayList<Triple<Place?, LatLng, LatLng>> = arrayListOf()
                    for (i in 0..(locations.size - 2)) {
                        triples.add(Triple(locations[i], locations[i]?.location.asLatLng(), locations[i + 1]?.location.asLatLng()))
                    }
            triples
                }
                .toObservable()
                .flatMapIterable { it }
                .observeOn(Schedulers.io())
                .flatMap { triple ->
                    placesRepository.getDirections(triple.second, triple.third)
                            .map { it.routes.first() }
                            .map { Pair(triple.first, decodePoly(it.overviewPolyline.points)) }
                            .toObservable()
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    d { "onSuccess " }
                    view?.addPolyline(it.second)
                    d { "addedPolyline" }
                    view?.highlightMarker(it.first)
                }, ::e)
    }

    private fun computeRoute(currentLocation: LatLng, filterDetails: SightsFilterDetails?) = placesRepository.getRoute(currentLocation, filterDetails)
            .subscribeOn(Schedulers.computation())
            .map {
                it.removeAll { it == null }
                it
            }
            .toObservable()
            .flatMapIterable { it }
            .toList()
            .doOnSuccess {
                saveRouteLocally(it)
            }

    private fun saveRouteLocally(route: List<Place?>) {
        userDetailsRepository.saveRoute(route)
    }
/*
   placesRepository.getRoute(it[0]?.location.asLatLng(), it[1]?.location.asLatLng()).toObservable()
                .map { it.routes.first() }
                .map { decodePoly(it.overviewPolyline.points) }
 */

    fun onPermissionsRefused() {
        d { "onPermissionsRefused" }
        view?.showAlertDialog(stringProvider.getString(R.string.location_permission_refused_dialog_title),
                stringProvider.getString(R.string.location_permission_refused_dialog_content))
        view?.askForLocationPermission()
    }

    fun onDestroy() {
        locationProvider.stopService()
    }

    private fun decodePoly(encoded: String): List<LatLng> {

        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            val p = LatLng(lat.toDouble() / 1E5,
                    lng.toDouble() / 1E5)
            poly.add(p)
        }

        return poly
    }

    fun requestRefresh() {
//        getPlacesNearby()
    }
}
