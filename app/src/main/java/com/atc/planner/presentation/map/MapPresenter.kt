package com.atc.planner.presentation.map

import android.location.Location
import com.atc.planner.R
import com.atc.planner.commons.BitmapProvider
import com.atc.planner.commons.LocationProvider
import com.atc.planner.commons.StringProvider
import com.atc.planner.data.models.local.LocalPlace
import com.atc.planner.di.scopes.FragmentScope
import com.atc.planner.extensions.asLatLng
import com.atc.planner.extensions.asLatLong
import com.atc.planner.extensions.dpToPx
import com.atc.planner.extensions.resize
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

    fun onSetData(items: List<LocalPlace>) {
        handlePlaces(items)
    }

    fun onAddData(items: List<LocalPlace>) {
        handlePlaces(items)
    }

    private fun handlePlaces(items: List<LocalPlace>) {
        if (isMapReady) {
            Observable.create<Pair<MarkerOptions, LocalPlace>> { emitter ->
                var counter = 1
                items.forEach { place ->
                    val markerOptions = MarkerOptions()
                    place.location?.asLatLng()?.let { placeLocation ->
                        markerOptions.position(placeLocation)
                        bitmapProvider.getRoundedBitmap(place.thumbnailUrl, R.drawable.error_marker, {
                            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(it?.resize(32.dpToPx().toInt(), 32.dpToPx().toInt())))
                            emitter.onNext(Pair(markerOptions, place))

                            counter++
                            if (counter == items.size) {
                                emitter.onComplete()
                            }
                        }, {
                            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(it.resize(32.dpToPx().toInt(), 32.dpToPx().toInt())))
                            emitter.onNext(Pair(markerOptions, place))

                            counter++
                            if (counter == items.size) {
                                emitter.onComplete()
                            }
                        })
                    }
                }
            }.subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe({ (markerOptions, place) ->
                        view?.addMarker(markerOptions, place)
                    }, ::e, {
                        view?.zoomToFitAllMarkers()
                    })
        }
    }

    fun onItemClick(localPlace: LocalPlace?) {
        view?.goToPlaceDetails(PlaceDetailsBundle(localPlace))
    }
}