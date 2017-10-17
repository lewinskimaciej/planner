package com.atc.planner.presentation.place_details

import com.atc.planner.di.scopes.ActivityScope
import com.atc.planner.presentation.base.BasePresenter
import java.io.Serializable
import javax.inject.Inject

@ActivityScope
class PlaceDetailsPresenter @Inject constructor(): BasePresenter<PlaceDetailsView>() {

    var placeDetailsBundle: PlaceDetailsBundle? = null

    override fun onViewCreated(data: Serializable?) {
        if (placeDetailsBundle == null) {
            placeDetailsBundle = data as? PlaceDetailsBundle
        }

        view?.setUpPlaceDetails(placeDetailsBundle?.localPlace)
    }
}