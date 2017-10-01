package com.atc.planner.presentation.map

import android.Manifest
import android.os.Bundle
import com.atc.planner.R
import com.atc.planner.presentation.base.BaseMvpFragment
import com.github.jksiezni.permissive.Permissive
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject

class MapFragment: BaseMvpFragment<MapView, MapPresenter>(), MapView, OnMapReadyCallback {

    override val layoutResId: Int
        get() = R.layout.fragment_map

    @Inject
    lateinit var mapPresenter: MapPresenter

    var map: GoogleMap? = null

    override fun createPresenter(): MapPresenter = mapPresenter

    override fun onViewCreated(savedInstanceState: Bundle?) {
        showMap()
    }

    override fun askForLocationPermission() {
        Permissive.Request(Manifest.permission.ACCESS_FINE_LOCATION)
                .whenPermissionsGranted {
                    presenter?.onPermissionsGranted()
                }
                .whenPermissionsRefused {
                    presenter?.onPermissionsRefused()
                }
                .execute(activity)
    }

    private fun showMap() {
        val supportMapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        supportMapFragment?.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap?) {
        this.map = map
        presenter?.onMapReady()
    }

    override fun showLocationOnMap(latLong: LatLng) {
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLong, 17f))
    }

}