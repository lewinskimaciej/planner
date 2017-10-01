package com.atc.planner.commons

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.support.v4.content.ContextCompat
import com.atc.planner.App
import com.github.ajalt.timberkt.d
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.lang.Exception
import javax.inject.Inject


interface LocationProvider {

    fun getLastLocation(onSuccess: ((location: Location?) -> Unit)? = null,
                        onFailure: ((exception: Exception) -> Unit)? = null)
}

class LocationProviderImpl @Inject constructor(var app: App) : LocationProvider {

    var fusedLocationProviderClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(app)


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
}