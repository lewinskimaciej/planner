package com.atc.planner.presentation.place_details

import android.content.Context
import android.os.Bundle
import com.atc.planner.R
import com.atc.planner.data.models.local.LocalPlace
import com.atc.planner.extensions.setupToolbarWithUpNavigation
import com.atc.planner.extensions.startActivity
import com.atc.planner.presentation.base.BaseDictionary
import com.atc.planner.presentation.base.BaseMvpActivity
import kotlinx.android.synthetic.main.activity_place_details.*
import javax.inject.Inject

class PlaceDetailsActivity : BaseMvpActivity<PlaceDetailsView, PlaceDetailsPresenter>(),
        PlaceDetailsView {

    override val layoutResId: Int?
        get() = R.layout.activity_place_details

    @Inject
    lateinit var placeDetailsPresenter: PlaceDetailsPresenter

    override fun createPresenter(): PlaceDetailsPresenter = placeDetailsPresenter

    companion object {
        fun start(from: Context, placeDetailsBundle: PlaceDetailsBundle) {
            val bundle = Bundle()
            bundle.putSerializable(BaseDictionary.KEY_SERIALIZABLE, placeDetailsBundle)
            from.startActivity(PlaceDetailsActivity::class, bundle)
        }
    }

    override fun onViewCreated(savedInstanceState: Bundle?) {
        setupToolbarWithUpNavigation(toolbar)
    }

    override fun setUpPlaceDetails(localPlace: LocalPlace?) {
        supportActionBar?.title = localPlace?.name
    }
}
