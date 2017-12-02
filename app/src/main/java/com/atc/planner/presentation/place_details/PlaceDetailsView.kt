package com.atc.planner.presentation.place_details

import com.atc.planner.data.model.local.Place
import com.atc.planner.presentation.base.BaseView

interface PlaceDetailsView : BaseView {
    fun setUpPlaceDetails(place: Place?)
}