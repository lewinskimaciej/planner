package com.atc.planner.presentation.main

import com.atc.planner.data.models.local.LocalPlace
import com.atc.planner.presentation.base.BaseView
import com.google.android.gms.maps.model.LatLng

interface MainView : BaseView {
    fun askForLocationPermission()
    fun setItems(items: List<LocalPlace>)
    fun addItems(items: List<LocalPlace>)
    fun addPolyline(polyline: List<LatLng>)
}
