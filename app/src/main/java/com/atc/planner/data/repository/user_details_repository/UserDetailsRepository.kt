package com.atc.planner.data.repository.user_details_repository

import com.atc.planner.data.model.local.Place
import com.atc.planner.data.repository.places_repository.SightsFilterDetails

interface UserDetailsRepository {
    fun saveFilterDetails(filterDetails: SightsFilterDetails?)
    fun getFilterDetails(): SightsFilterDetails?
    fun saveRoute(route: List<Place?>)
    fun getRoute(): List<Place?>
}