package com.atc.planner.commons

import android.Manifest
import android.location.Location
import com.atc.planner.App
import com.github.ajalt.timberkt.d
import com.github.jksiezni.permissive.Permissive
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.lang.Exception
import javax.inject.Inject


interface LocationProvider {

    fun getLastLocation(onSuccess: ((location: Location?) -> Unit)? = null,
                        onFailure: ((exception: Exception?) -> Unit)? = null)
}

class LocationProviderImpl @Inject constructor(var app: App) : LocationProvider {

    var fusedLocationProviderClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(app)


    override fun getLastLocation(onSuccess: ((location: Location?) -> Unit)?,
                                 onFailure: ((exception: Exception?) -> Unit)?) {
        d { "getting last location" }
        val hasPermission = Permissive.checkPermission(app, Manifest.permission.ACCESS_FINE_LOCATION)
        if (hasPermission) {
            val task = fusedLocationProviderClient.lastLocation

            task.addOnCompleteListener {
                if (it.isSuccessful) {
                    d { "successful, result: ${it.result}" }
                    onSuccess?.invoke(it.result)
                } else {
                    d { "failure, exception: ${it.exception?.message}" }
                    onFailure?.invoke(it.exception)
                }
            }
        } else {
            onFailure?.invoke(SecurityException("Manifest.permission.ACCESS_FINE_LOCATION not available"))
        }
    }
}