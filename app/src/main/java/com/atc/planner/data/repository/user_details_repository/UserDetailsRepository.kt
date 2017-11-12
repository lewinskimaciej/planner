package com.atc.planner.data.repository.user_details_repository

import com.atc.planner.data.repository.places_nearby_repository.SightsFilterDetails

interface UserDetailsRepository {
    fun saveFilterDetails(filterDetails: SightsFilterDetails?)
    fun getFilterDetails(): SightsFilterDetails?
}