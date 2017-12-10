package com.atc.planner.presentation.map

import com.atc.planner.data.model.local.Place
import com.atc.planner.presentation.base.BaseView
import com.atc.planner.presentation.place_details.PlaceDetailsBundle
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

interface MapView : BaseView {
    fun askForLocationPermission()
    fun showCurrentLocation(latLong: LatLng)
    fun setData(items: List<Place>)
    fun addData(items: List<Place>)
    fun addMarker(options: MarkerOptions?, place: Place)
    fun clearMarkers()
    fun zoomToFitAllMarkers()
    fun goToPlaceDetails(placeDetailsBundle: PlaceDetailsBundle)
    fun drawPolyline(polyline: List<LatLng>)
    fun highlightMarker(place: Place?)
    fun clearPolyline()
}