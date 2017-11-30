package com.atc.planner.presentation.place_details

import com.atc.planner.data.models.local.Place
import com.atc.planner.presentation.base.BaseView

interface PlaceDetailsView : BaseView {
    fun setUpPlaceDetails(place: Place?)
}