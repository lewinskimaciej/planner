package com.atc.planner.presentation.main

import com.atc.planner.R
import com.atc.planner.commons.LocationProvider
import com.atc.planner.commons.StringProvider
import com.atc.planner.di.scopes.ActivityScope
import com.atc.planner.presentation.base.BasePresenter
import java.io.Serializable
import javax.inject.Inject

@ActivityScope
class MainPresenter @Inject constructor(private val stringProvider: StringProvider,
                                        private val locationProvider: LocationProvider)
    : BasePresenter<MainView>() {

    override fun onViewCreated(data: Serializable?) {

    }

    fun onPermissionsGranted() {

    }

    fun onPermissionsRefused() {
        view?.showAlertDialog(stringProvider.getString(R.string.location_permission_refused_dialog_title),
                stringProvider.getString(R.string.location_permission_refused_dialog_contemt))
        view?.askForLocationPermission()
    }
}