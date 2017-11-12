package com.atc.planner.data.repository.user_details_repository

import android.content.SharedPreferences
import com.atc.planner.data.repository.places_nearby_repository.SightsFilterDetails
import com.google.gson.Gson
import javax.inject.Inject

class UserDetailsRepositoryImpl @Inject constructor(private val sharedPreferences: SharedPreferences,
                                                    private val gson: Gson) : UserDetailsRepository {

    override fun saveFilterDetails(filterDetails: SightsFilterDetails?) {
        sharedPreferences.edit().putString(SightsFilterDetails::class.java.simpleName, gson.toJson(filterDetails)).commit()
    }

    override fun getFilterDetails(): SightsFilterDetails? {
        val defaultJson = gson.toJson(SightsFilterDetails())
        val json = sharedPreferences.getString(SightsFilterDetails::class.java.simpleName, defaultJson)

        return gson.fromJson(json, SightsFilterDetails::class.java)
    }
}