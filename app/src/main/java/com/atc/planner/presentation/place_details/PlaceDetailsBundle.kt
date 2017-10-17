package com.atc.planner.presentation.place_details

import com.atc.planner.data.models.local.LocalPlace
import com.atc.planner.presentation.base.BaseBundle

data class PlaceDetailsBundle(var localPlace: LocalPlace?): BaseBundle()