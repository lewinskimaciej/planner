package com.atc.planner.presentation.map

import android.graphics.BitmapFactory
import android.os.Bundle
import com.atc.planner.R
import com.atc.planner.commons.BitmapProvider
import com.atc.planner.data.models.local.LocalPlace
import com.atc.planner.extensions.dpToPx
import com.atc.planner.extensions.resize
import com.atc.planner.presentation.base.BaseMvpFragment
import com.github.ajalt.timberkt.e
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.tbruyelle.rxpermissions2.RxPermissions
import javax.inject.Inject

class MapFragment : BaseMvpFragment<MapView, MapPresenter>(), MapView, OnMapReadyCallback {

    override val layoutResId: Int
        get() = R.layout.fragment_map

    @Inject
    lateinit var mapPresenter: MapPresenter

    @Inject
    lateinit var bitmapProvider: BitmapProvider

    var map: GoogleMap? = null

    lateinit var rxPermissions: RxPermissions

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
    }

    override fun showLocationOnMap(latLong: LatLng) {
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLong, 17f))
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
            markerOptions.position(it)
            bitmapProvider.getRoundedBitmap(item.thumbnailUrl,
                    {
                        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(it?.resize(32.dpToPx().toInt(), 32.dpToPx().toInt())))
                        map?.addMarker(markerOptions)
                    },
                    {
                        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.error_marker)

                        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap.resize(32.dpToPx().toInt(), 32.dpToPx().toInt())))
                        map?.addMarker(markerOptions)
                    })

        }
    }

}