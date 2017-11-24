package com.atc.planner.presentation.main

import com.atc.planner.R
import com.atc.planner.commons.LocationProvider
import com.atc.planner.commons.StringProvider
import com.atc.planner.data.repository.places_nearby_repository.PlacesNearbyRepository
import com.atc.planner.data.repository.places_nearby_repository.SightsFilterDetails
import com.atc.planner.data.repository.user_details_repository.UserDetailsRepository
import com.atc.planner.di.scopes.ActivityScope
import com.atc.planner.extensions.asLatLng
import com.atc.planner.extensions.asLatLong
import com.atc.planner.extensions.orZero
import com.atc.planner.presentation.base.BaseMvpPresenter
import com.github.ajalt.timberkt.d
import com.github.ajalt.timberkt.e
import com.google.android.gms.maps.model.LatLng
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.Serializable
import javax.inject.Inject

@ActivityScope
class MainPresenter @Inject constructor(private val stringProvider: StringProvider,
                                        private val locationProvider: LocationProvider,
                                        private val placesNearbyRepository: PlacesNearbyRepository,
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
//            if (count == 0) {
//                count++
//                currentLocation?.let {
//                    placesNearbyRepository.getPlacesFromSygic(it)
//                            .subscribe({ d { "got all places: ${it.size}" } }, ::e)
//                }
//            }
        }, {
            e(it)
        })
        locationProvider.startService()
    }

    private fun getPlacesNearby() {
        if (lastRefreshDate == null || System.currentTimeMillis() - lastRefreshDate.orZero() > 2000) {
            currentLocation?.let { location ->
                val filterDetails = userDetailsRepository.getFilterDetails()

                d { "getSightsNearby" }
                placesNearbyRepository.getSightsNearby(location, filterDetails)
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            d { "got ${it.size} items" }
                            view?.setItems(it)
                            getDirections(location, filterDetails)
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

    private fun getDirections(currentLocation: LatLng, filterDetails: SightsFilterDetails?) {
        placesNearbyRepository.getRoad(currentLocation, filterDetails)
                .subscribeOn(Schedulers.computation())
                .map {
                    it.removeAll { it == null }
                    it
                }
                .toObservable()
                .flatMapIterable { it }
                .doOnNext { d { "FOUND: ${it.name}" } }
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ locations ->
                    d { "onSuccess " }
                    val pairs: ArrayList<Pair<LatLng, LatLng>> = arrayListOf()
                    for (i in 0..(locations.size - 2)) {
                        pairs.add(Pair(locations[i]?.location.asLatLng(), locations[i + 1]?.location.asLatLng()))
                    }

                    pairs.forEach {
                        d { "getting polyline for $it" }
                        placesNearbyRepository.getDirections(it.first, it.second)
                                .subscribeOn(Schedulers.io())
                                .map { it.routes.first() }
                                .map { decodePoly(it.overviewPolyline.points) }
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({
                                    view?.addPolyline(it)
                                    d { "addedPolyline" }
                                }, ::e)
                    }
                }, ::e)
    }
/*
   placesNearbyRepository.getDirections(it[0]?.location.asLatLng(), it[1]?.location.asLatLng()).toObservable()
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
