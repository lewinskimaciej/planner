package com.atc.planner.presentation.main

import com.atc.planner.R
import com.atc.planner.commons.LocationProvider
import com.atc.planner.commons.StringProvider
import com.atc.planner.data.repository.places_nearby_repository.PlacesNearbyRepository
import com.atc.planner.di.scopes.ActivityScope
import com.atc.planner.extensions.asLatLong
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
                                        private val placesNearbyRepository: PlacesNearbyRepository)
    : BaseMvpPresenter<MainView>() {

    private var currentLocation: LatLng? = null

    override fun onViewCreated(data: Serializable?) {
        view?.askForLocationPermission()
    }

    fun onPermissionsGranted() {
        d { "onPermissionsGranted" }
        locationProvider.getLastLocation({
            currentLocation = it?.asLatLong()

            currentLocation?.let {
                placesNearbyRepository.getSightsNearby(it)
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            view?.setItems(it)
                        }, ::e)
            }
        }, {
            e(it)
        })
        locationProvider.startService()
    }

    private fun getDirections(source: LatLng, dest: LatLng) {
        placesNearbyRepository.getDirections(source, dest)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .toObservable()
                .flatMapIterable { it.routes }
                .firstOrError()
                .map { decodePoly(it.overviewPolyline.points) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    d { it.toString() }
                    view?.addPolyline(it)
                }, ::e)
    }

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
}
