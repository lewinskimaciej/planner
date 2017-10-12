package com.atc.planner.presentation.map

import com.atc.planner.R
import com.atc.planner.commons.LocationProvider
import com.atc.planner.commons.StringProvider
import com.atc.planner.data.models.local.LocalPlace
import com.atc.planner.di.scopes.FragmentScope
import com.atc.planner.extensions.asLatLng
import com.atc.planner.presentation.base.BasePresenter
import com.github.ajalt.timberkt.e
import com.google.android.gms.maps.model.LatLng
import java.io.Serializable
import javax.inject.Inject
import com.google.android.gms.maps.model.LatLngBounds



@FragmentScope
class MapPresenter @Inject constructor(private val stringProvider: StringProvider,
                                       private val locationProvider: LocationProvider)
    : BasePresenter<MapView>() {

    companion object {
        private val defaultLocation = LatLng(51.108964, 17.060151)
    }

    private var currentLocation: LatLng? = null
    private var isMapReady = false

    override fun onViewCreated(data: Serializable?) {
        view?.askForLocationPermission()
    }

    fun onPermissionsGranted() {

    }

    fun onPermissionsRefused() {
        view?.showAlertDialog(stringProvider.getString(R.string.location_permission_refused_dialog_title),
                stringProvider.getString(R.string.location_permission_refused_dialog_content))
        view?.askForLocationPermission()
    }

    fun onMapReady() {
        isMapReady = true

        locationProvider.getLastLocation({
            currentLocation = it?.asLatLng()
            showCurrentLocation()
        }, {
            it.let {
                e(it)
                view?.showErrorToast()
                showDefaultLocation()
            }
        })
    }

    private fun showCurrentLocation() {
        if (currentLocation != null) {
            currentLocation?.let {
                view?.showCurrentLocation(it)
            }
        } else {
            showDefaultLocation()
        }
    }

    private fun showDefaultLocation() {
        view?.showCurrentLocation(defaultLocation)
    }

    fun onSetData(items: List<LocalPlace>) {
        if (isMapReady) {
            items.forEach { view?.addMarker(it) }
            view?.zoomToFitAllMarkers()
        }
    }

    fun onAddData(items: List<LocalPlace>) {
        if (isMapReady) {
            items.forEach { view?.addMarker(it) }
        }
    }

    fun onItemClick(localPlace: LocalPlace?){
        // todo
    }
}