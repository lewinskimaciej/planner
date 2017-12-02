package com.atc.planner.data.repository.user_details_repository

import android.content.SharedPreferences
import com.atc.planner.data.models.local.Place
import com.atc.planner.data.repository.places_nearby_repository.SightsFilterDetails
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
}