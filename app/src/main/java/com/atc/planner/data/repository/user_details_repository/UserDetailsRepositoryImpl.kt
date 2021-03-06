package com.atc.planner.data.repository.user_details_repository

import android.content.SharedPreferences
import com.atc.planner.data.model.local.BeaconSeenEvent
import com.atc.planner.data.model.local.Place
import com.atc.planner.data.repository.places_repository.SightsFilterDetails
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

class UserDetailsRepositoryImpl @Inject constructor(private val sharedPreferences: SharedPreferences,
                                                    private val gson: Gson) : UserDetailsRepository {

    override fun saveFilterDetails(filterDetails: SightsFilterDetails?) {
        sharedPreferences.edit().putString(SightsFilterDetails::class.java.simpleName, gson.toJson(filterDetails)).apply()
    }

    override fun getFilterDetails(): SightsFilterDetails? {
        val defaultJson = gson.toJson(SightsFilterDetails())
        val json = sharedPreferences.getString(SightsFilterDetails::class.java.simpleName, defaultJson)

        return gson.fromJson(json, SightsFilterDetails::class.java)
    }

    override fun saveRoute(route: List<Place?>) {
        val json = gson.toJson(route)
        sharedPreferences.edit().putString("route", json).apply()
    }

    override fun getRoute(): List<Place?> {
        val json = sharedPreferences.getString("route", gson.toJson(listOf<Place?>()))
        return gson.fromJson(json, object : TypeToken<List<Place?>>() {}.type)
    }

    override fun saveSeenBeacons(beacons: List<BeaconSeenEvent?>) {
        val json = gson.toJson(beacons)
        sharedPreferences.edit().putString("seenBeacons", json).apply()
    }

    override fun getSeenBeacons(): List<BeaconSeenEvent?> {
        val json = sharedPreferences.getString("seenBeacons", gson.toJson(listOf<BeaconSeenEvent?>()))
        return gson.fromJson(json, object : TypeToken<List<BeaconSeenEvent?>>() {}.type)
    }
}
