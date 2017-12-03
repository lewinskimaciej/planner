package com.atc.planner.presentation.settings

import com.atc.planner.data.repository.places_repository.SightsFilterDetails
import com.atc.planner.presentation.base.BaseView

interface SettingsView : BaseView {
    fun setUpValues(filterDetails: SightsFilterDetails?)
}