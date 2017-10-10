package com.atc.planner.presentation.map

import com.atc.planner.data.models.local.LocalPlace
import com.atc.planner.presentation.base.BaseView
import com.google.android.gms.maps.model.LatLng

interface MapView: BaseView {
    fun askForLocationPermission()
    fun showLocationOnMap(latLong: LatLng)
    fun setData(items: List<LocalPlace>)
    fun addData(items: List<LocalPlace>)
    fun addMarker(item: LocalPlace)
}