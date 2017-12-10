package com.atc.planner.presentation.settings

import com.atc.planner.data.repository.places_repository.SightsFilterDetails
import com.atc.planner.data.repository.user_details_repository.UserDetailsRepository
import com.atc.planner.presentation.base.BaseMvpPresenter
import java.io.Serializable
import javax.inject.Inject

class SettingsPresenter @Inject constructor(private val userDetailsRepository: UserDetailsRepository) : BaseMvpPresenter<SettingsView>() {

    private var originalFilterDetails: SightsFilterDetails? = null
    private var filterDetails: SightsFilterDetails? = null

    override fun onViewCreated(data: Serializable?) {
        if (filterDetails == null) {
            filterDetails = userDetailsRepository.getFilterDetails()
            originalFilterDetails = filterDetails?.copy()
        }

        view?.setUpValues(filterDetails)
    }

    private fun updateFilterDetails() {
        userDetailsRepository.saveFilterDetails(filterDetails)
    }

    fun onIAmAChildCheckboxChanges(checked: Boolean) {
        filterDetails?.targetsChildren = checked
        updateFilterDetails()
    }

    fun onMuseumCheckboxChanges(checked: Boolean) {
        filterDetails?.canBeAMuseum = checked
        updateFilterDetails()
    }

    fun onArtGalleriesCheckboxChanges(checked: Boolean) {
        filterDetails?.canBeAnArtGallery = checked
        updateFilterDetails()
    }

    fun onPhysicalActivityCheckboxChanges(checked: Boolean) {
        filterDetails?.canBePhysicalActivity = checked
        updateFilterDetails()
    }

    fun onMaxEntryFeeChanges(newMaxEntryFee: Float) {
        filterDetails?.maxEntryFee = newMaxEntryFee
        updateFilterDetails()
    }

    fun end() {
        view?.endWithResult(originalFilterDetails?.equals(filterDetails) == false)
    }

}