package com.atc.planner.presentation.main

import com.atc.planner.data.models.local.LocalPlace
import com.atc.planner.presentation.base.BaseView

interface MainView : BaseView {
    fun askForLocationPermission()
    fun setItems(items: List<LocalPlace>)
    fun addItems(items: List<LocalPlace>)
}
