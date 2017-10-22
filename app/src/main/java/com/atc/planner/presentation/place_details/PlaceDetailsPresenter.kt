package com.atc.planner.presentation.place_details

import com.atc.planner.di.scopes.ActivityScope
import com.atc.planner.presentation.base.BaseMvpPresenter
import java.io.Serializable
import javax.inject.Inject

@ActivityScope
class PlaceDetailsPresenter @Inject constructor(): BaseMvpPresenter<PlaceDetailsView>() {

    private var placeDetailsBundle: PlaceDetailsBundle? = null

    override fun onViewCreated(data: Serializable?) {
        if (placeDetailsBundle == null) {
            placeDetailsBundle = data as? PlaceDetailsBundle
        }

        view?.setUpPlaceDetails(placeDetailsBundle?.localPlace)
    }

    override fun onNewBundle(data: Serializable?) {
        onViewCreated(data)
    }
}