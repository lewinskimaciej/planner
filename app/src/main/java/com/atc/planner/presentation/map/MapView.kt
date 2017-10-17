package com.atc.planner.presentation.map

import com.atc.planner.data.models.local.LocalPlace
import com.atc.planner.presentation.base.BaseView
import com.atc.planner.presentation.place_details.PlaceDetailsBundle
import com.google.android.gms.maps.model.LatLng

interface MapView : BaseView {
    fun askForLocationPermission()
    fun showCurrentLocation(latLong: LatLng)
    fun setData(items: List<LocalPlace>)
    fun addData(items: List<LocalPlace>)
    fun addMarker(item: LocalPlace)
    fun clearMarkers()
    fun zoomToFitAllMarkers()
    fun goToPlaceDetails(placeDetailsBundle: PlaceDetailsBundle)
}