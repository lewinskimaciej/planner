package com.atc.planner.presentation.map

import android.graphics.BitmapFactory
import android.os.Bundle
import com.atc.planner.R
import com.atc.planner.commons.BitmapProvider
import com.atc.planner.data.models.local.LocalPlace
import com.atc.planner.extensions.asLatLng
import com.atc.planner.extensions.dpToPx
import com.atc.planner.extensions.resize
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

    @Inject
    lateinit var bitmapProvider: BitmapProvider

    lateinit var rxPermissions: RxPermissions

    private var map: GoogleMap? = null
    private var usersLocationMarker: Marker? = null
    private var currentItems: HashMap<Marker?, LocalPlace> = hashMapOf()

    override fun createPresenter(): MapPresenter = mapPresenter

    override fun onViewCreated(savedInstanceState: Bundle?) {
        rxPermissions = RxPermissions(activity)
        showMap()
    }

    override fun askForLocationPermission() {
        rxPermissions.request(android.Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe({
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

    override fun setData(items: List<LocalPlace>) {
        presenter?.onSetData(items)
    }

    override fun addData(items: List<LocalPlace>) {
        presenter?.onAddData(items)
    }

    override fun clearMarkers() {
        map?.clear()
    }

    override fun addMarker(item: LocalPlace) {
        item.location?.let {
            val markerOptions = MarkerOptions()
            markerOptions.position(it.asLatLng())
            bitmapProvider.getRoundedBitmap(item.thumbnailUrl,
                    {
                        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(it?.resize(32.dpToPx().toInt(), 32.dpToPx().toInt())))
                        val marker = map?.addMarker(markerOptions)
                        currentItems.put(marker, item)
                    },
                    {
                        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.error_marker)

                        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap.resize(32.dpToPx().toInt(), 32.dpToPx().toInt())))
                        val marker = map?.addMarker(markerOptions)
                        currentItems.put(marker, item)
                    })

        }
    }

    override fun zoomToFitAllMarkers() {
        val builder = LatLngBounds.Builder()
        currentItems.forEach { builder.include(it.value.location?.asLatLng()) }
        val bounds = builder.build()
        map?.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 20))
    }

    override fun goToPlaceDetails(placeDetailsBundle: PlaceDetailsBundle) {
        PlaceDetailsActivity.start(context, placeDetailsBundle)
    }
}