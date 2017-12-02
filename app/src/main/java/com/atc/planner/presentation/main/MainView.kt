package com.atc.planner.presentation.main

import com.atc.planner.data.model.local.Place
import com.atc.planner.presentation.base.BaseView
import com.google.android.gms.maps.model.LatLng

interface MainView : BaseView {
    fun askForLocationPermission()
    fun setItems(items: List<Place>)
    fun addItems(items: List<Place>)
    fun addPolyline(polyline: List<LatLng>)
    fun highlightMarker(place: Place?)
}
