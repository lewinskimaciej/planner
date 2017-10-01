package com.atc.planner.presentation.map

import android.location.Location
import com.atc.planner.R
import com.atc.planner.commons.LocationProvider
import com.atc.planner.commons.StringProvider
import com.atc.planner.di.scopes.FragmentScope
import com.atc.planner.presentation.base.BasePresenter
import com.github.ajalt.timberkt.e
import com.google.android.gms.maps.model.LatLng
import java.io.Serializable
import javax.inject.Inject

@FragmentScope
class MapPresenter @Inject constructor(private val stringProvider: StringProvider,
                                       private val locationProvider: LocationProvider)
    : BasePresenter<MapView>() {

    companion object {
        private val defaultLocation = LatLng(51.108964, 17.060151)
    }

    private var currentLocation: Location? = null
    private var isMapReady = false

    override fun onViewCreated(data: Serializable?) {
        view?.askForLocationPermission()
    }

    fun onPermissionsGranted() {

    }

    fun onPermissionsRefused() {
        view?.showAlertDialog(stringProvider.getString(R.string.location_permission_refused_dialog_title),
                stringProvider.getString(R.string.location_permission_refused_dialog_contemt))
        view?.askForLocationPermission()
    }

    fun onMapReady() {
        isMapReady = true

        locationProvider.getLastLocation({
            currentLocation = it
            showCurrentLocation()
        }, {
            it?.let {
                e(it)
                view?.showErrorToast()
                showDefaultLocation()
            }
        })
    }

    private fun showCurrentLocation() {
        if (currentLocation != null) {
            currentLocation?.let {
                view?.showLocationOnMap(LatLng(it.latitude, it.longitude))
            }
        } else {
            showDefaultLocation()
        }
    }

    private fun showDefaultLocation() {
        view?.showLocationOnMap(defaultLocation)
    }
}