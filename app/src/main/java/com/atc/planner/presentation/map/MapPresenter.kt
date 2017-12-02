package com.atc.planner.presentation.map

import android.location.Location
import com.atc.planner.R
import com.atc.planner.commons.BitmapProvider
import com.atc.planner.commons.LocationProvider
import com.atc.planner.commons.StringProvider
import com.atc.planner.data.model.local.Place
import com.atc.planner.di.scopes.FragmentScope
import com.atc.planner.extension.asLatLng
import com.atc.planner.extension.asLatLong
import com.atc.planner.presentation.base.BaseMvpPresenter
import com.atc.planner.presentation.place_details.PlaceDetailsBundle
import com.github.ajalt.timberkt.Timber.e
import com.google.android.gms.location.LocationListener
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.Serializable
import javax.inject.Inject


@FragmentScope
class MapPresenter @Inject constructor(private val stringProvider: StringProvider,
                                       private val locationProvider: LocationProvider,
                                       private var bitmapProvider: BitmapProvider)
    : BaseMvpPresenter<MapView>(), LocationListener {

    companion object {
        private val defaultLocation = LatLng(51.108964, 17.060151)
    }

    private var currentLocation: LatLng? = null
    private var isMapReady = false

    override fun onViewCreated(data: Serializable?) {
        view?.askForLocationPermission()
    }

    override fun detachView(retainInstance: Boolean) {
        locationProvider.removeListener(this)
        super.detachView(retainInstance)
    }

    fun onPermissionsGranted() {

    }

    fun onPermissionsRefused() {
        view?.showAlertDialog(stringProvider.getString(R.string.location_permission_refused_dialog_title),
                stringProvider.getString(R.string.location_permission_refused_dialog_content))
        view?.askForLocationPermission()
    }

    fun onMapReady() {
        isMapReady = true

        locationProvider.getLastLocation({
            currentLocation = it?.asLatLong()
            showCurrentLocation()
            locationProvider.addListener(this)
        }, {
            it.let {
                e(it)
                view?.showErrorToast()
                showDefaultLocation()
            }
        })
    }

    override fun onLocationChanged(p0: Location?) {
        currentLocation = p0?.asLatLong()
        showCurrentLocation()
    }

    private fun showCurrentLocation() {
        if (currentLocation != null) {
            currentLocation?.let {
                view?.showCurrentLocation(it)
                view?.zoomToFitAllMarkers()
            }
        } else {
            showDefaultLocation()
        }
    }

    private fun showDefaultLocation() {
        view?.showCurrentLocation(defaultLocation)
    }

    fun onSetData(items: List<Place>) {
        handlePlaces(items)
    }

    fun onAddData(items: List<Place>) {
        handlePlaces(items)
    }

    private fun handlePlaces(items: List<Place>) {
        if (isMapReady) {
            Observable.create<Pair<MarkerOptions, Place>> { emitter ->
                var counter = 1
                for (index in items.indices) {
                    val markerOptions = MarkerOptions()

                    val placeLocation = items[index].location?.asLatLng()
                    if (placeLocation != null) {
                        markerOptions.position(placeLocation)
                        bitmapProvider.getRoundedBitmap(items[index].thumbnailUrl, R.drawable.error_marker, {
                            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(it))
                            emitter.onNext(Pair(markerOptions, items[index]))

                            counter++
                            if (counter == items.size) {
                                emitter.onComplete()
                            }
                        }, {
                            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(it))
                            emitter.onNext(Pair(markerOptions, items[index]))

                            counter++
                            if (counter == items.size) {
                                emitter.onComplete()
                            }
                        })
                    }
                }
            }.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ (markerOptions, place) ->
                        view?.addMarker(markerOptions, place)
                    }, ::e, {
                        view?.zoomToFitAllMarkers()
                    })
        }
    }

    fun onItemClick(place: Place?) {
        view?.goToPlaceDetails(PlaceDetailsBundle(place))
    }
}