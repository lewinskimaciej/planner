package com.atc.planner.presentation.main

import com.atc.planner.presentation.base.BaseView
import com.atc.planner.presentation.main.adapter.PlaceItem
import com.google.android.gms.maps.model.LatLng

interface MainView : BaseView {
    fun askForLocationPermission()
    fun setItems(items: List<PlaceItem>)
}