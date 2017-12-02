package com.atc.planner.presentation.map

import android.graphics.Color
import android.os.Bundle
import com.atc.planner.R
import com.atc.planner.data.models.local.Place
import com.atc.planner.extensions.asLatLng
import com.atc.planner.presentation.base.BaseMvpFragment
import com.atc.planner.presentation.place_details.PlaceDetailsActivity
import com.atc.planner.presentation.place_details.PlaceDetailsBundle
import com.github.ajalt.timberkt.e
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.tbruyelle.rxpermissions2.RxPermissions
import javax.inject.Inject

class MapFragment : BaseMvpFragment<MapView, MapPresenter>(), MapView, OnMapReadyCallback {

    override val layoutResId: Int
        get() = R.layout.fragment_map

    @Inject
    lateinit var mapPresenter: MapPresenter

    var rxPermissions: RxPermissions? = null

    private var map: GoogleMap? = null
    private var usersLocationMarker: Marker? = null
    private var currentItems: HashMap<Marker?, Place> = hashMapOf()

    override fun createPresenter(): MapPresenter = mapPresenter

    override fun onViewCreated(savedInstanceState: Bundle?) {
        activity?.let {
            rxPermissions = RxPermissions(it)
        }
        showMap()
    }

    override fun askForLocationPermission() {
        rxPermissions?.request(android.Manifest.permission.ACCESS_FINE_LOCATION)
                ?.subscribe({
                    if (it) {
                        presenter?.onPermissionsGranted()
                    } else {
                        presenter?.onPermissionsRefused()
                    }
                }, ::e)
    }

    private fun showMap() {
        val supportMapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        supportMapFragment?.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap?) {
        this.map = map
        presenter?.onMapReady()
        map?.setOnMarkerClickListener {
            presenter?.onItemClick(currentItems[it])
            false
        }
    }

    override fun showCurrentLocation(latLong: LatLng) {
        if (usersLocationMarker == null) {
            val markerOptions = MarkerOptions()
            markerOptions.position(latLong)
            usersLocationMarker = map?.addMarker(markerOptions)
        } else {
            usersLocationMarker?.position = latLong
        }
    }

    override fun setData(items: List<Place>) {
        presenter?.onSetData(items)
    }

    override fun addData(items: List<Place>) {
        presenter?.onAddData(items)
    }

    override fun clearMarkers() {
        map?.clear()
    }

    override fun addMarker(options: MarkerOptions?, place: Place) {
        val marker = map?.addMarker(options)
        currentItems.put(marker, place)
    }

    override fun zoomToFitAllMarkers() {
        if (currentItems.isNotEmpty()) {
            val builder = LatLngBounds.Builder()
            currentItems.forEach { builder.include(it.value.location?.asLatLng()) }
            val bounds = builder.build()
            map?.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 20))
        } else if (usersLocationMarker != null) {
            map?.animateCamera(CameraUpdateFactory.newLatLngZoom(usersLocationMarker?.position, 14f))
        }
    }

    override fun goToPlaceDetails(placeDetailsBundle: PlaceDetailsBundle) {
        context?.let {
            PlaceDetailsActivity.start(it, placeDetailsBundle)
        }
    }

    override fun drawPolyline(polyline: List<LatLng>) {
        val options = PolylineOptions()
        polyline.forEach { options.add(it) }
        options.width(12f)
        options.color(Color.parseColor("#05b1fb"))
        map?.addPolyline(options)
    }

    override fun highlightMarker(place: Place?) {
        // todo
    }
}