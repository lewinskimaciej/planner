package com.atc.planner.commons

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.content.ContextCompat
import com.atc.planner.App
import com.atc.planner.data.service.SearchService
import com.github.ajalt.timberkt.d
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.lang.Exception
import javax.inject.Inject


interface LocationProvider : android.location.LocationListener {

    fun getLastLocation(onSuccess: ((location: Location?) -> Unit)? = null,
                        onFailure: ((exception: Exception) -> Unit)? = null)

    fun addListener(listener: com.google.android.gms.location.LocationListener)
    fun removeListener(listener: com.google.android.gms.location.LocationListener)

    fun startService()
    fun stopService()
}

class LocationProviderImpl @Inject constructor(var app: App) : LocationProvider {
    private var fusedLocationProviderClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(app)
    private var locationManager: LocationManager? = app.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    private var listeners: ArrayList<com.google.android.gms.location.LocationListener> = arrayListOf()

    override fun getLastLocation(onSuccess: ((location: Location?) -> Unit)?,
                                 onFailure: ((exception: Exception) -> Unit)?) {
        d { "getting last location" }
        val checkSelfPermission = ContextCompat.checkSelfPermission(app, Manifest.permission.ACCESS_FINE_LOCATION)
        if (checkSelfPermission == PackageManager.PERMISSION_GRANTED) {
            val task = fusedLocationProviderClient.lastLocation

            task.addOnCompleteListener {
                if (it.isSuccessful) {
                    d { "successful, result: ${it.result}" }
                    onSuccess?.invoke(it.result)
                } else {
                    it.exception?.let {
                        d { "failure, exception: ${it.message}" }
                        onFailure?.invoke(it)
                    }
                }
            }
        } else {
            onFailure?.invoke(SecurityException("Manifest.permission.ACCESS_FINE_LOCATION not available"))
        }
    }

    override fun addListener(listener: com.google.android.gms.location.LocationListener) {
        val permissionCheck = ContextCompat.checkSelfPermission(app, Manifest.permission.ACCESS_FINE_LOCATION)
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            if (listeners.isEmpty()) {
                locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10.toFloat(), this)
            }
            listeners.add(listener)
        }
    }

    override fun removeListener(listener: com.google.android.gms.location.LocationListener) {
        listeners.remove(listener)
        if (listeners.isEmpty()) {
            locationManager?.removeUpdates(this)
        }
    }

    override fun onLocationChanged(p0: Location?) {
        listeners.forEach { it.onLocationChanged(p0) }
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
        // ignore
    }

    override fun onProviderEnabled(p0: String?) {
        // ignore
    }

    override fun onProviderDisabled(p0: String?) {
        // ignore
    }

    override fun startService() {
        val intent = Intent(app, SearchService::class.java)
        app.startService(intent)
    }

    override fun stopService() {
        val intent = Intent(app, SearchService::class.java)
        app.stopService(intent)
    }
}