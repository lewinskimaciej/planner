package com.atc.planner.commons

import android.location.Geocoder
import com.atc.planner.App
import com.google.android.gms.maps.model.LatLng
import java.io.IOException
import java.util.*
import javax.inject.Inject

interface CityProvider {
    fun getCity(latLng: LatLng): String?
}

class CityProviderImpl @Inject constructor(val app: App) : CityProvider {

    var geoCoder: Geocoder = Geocoder(app, Locale.getDefault())

    override fun getCity(latLng: LatLng): String? {
        return try {
            val fromLocation = geoCoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            if (fromLocation.isEmpty()) {
                null
            } else {
                fromLocation.first().subAdminArea
            }
        } catch (e: IOException) {
            null
        } catch (e: NullPointerException) {
            null
        }
    }

}