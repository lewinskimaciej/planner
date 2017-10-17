package com.atc.planner.presentation.place_details

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.atc.planner.R
import com.atc.planner.data.models.local.LocalPlace
import com.atc.planner.extensions.setupToolbarWithUpNavigation
import com.atc.planner.extensions.startActivity
import com.atc.planner.presentation.base.BaseDictionary
import com.atc.planner.presentation.base.BaseMvpActivity
import com.atc.planner.presentation.place_details.adapter.PictureItem
import com.github.ajalt.timberkt.e
import com.jakewharton.rxbinding2.view.clicks
import com.mikepenz.fastadapter.IItem
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import kotlinx.android.synthetic.main.activity_place_details.*
import kotlinx.android.synthetic.main.content_place_details.*
import javax.inject.Inject


class PlaceDetailsActivity : BaseMvpActivity<PlaceDetailsView, PlaceDetailsPresenter>(),
        PlaceDetailsView {

    override val layoutResId: Int?
        get() = R.layout.activity_place_details

    @Inject
    lateinit var placeDetailsPresenter: PlaceDetailsPresenter

    override fun createPresenter(): PlaceDetailsPresenter = placeDetailsPresenter

    private val fastAdapter: FastItemAdapter<IItem<*, *>> = FastItemAdapter()

    companion object {
        fun start(from: Context, placeDetailsBundle: PlaceDetailsBundle) {
            val bundle = Bundle()
            bundle.putSerializable(BaseDictionary.KEY_SERIALIZABLE, placeDetailsBundle)
            from.startActivity(PlaceDetailsActivity::class, bundle)
        }
    }

    override fun onViewCreated(savedInstanceState: Bundle?) {
        setupToolbarWithUpNavigation(toolbar)

        photos_recycler_view?.adapter = fastAdapter

        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        photos_recycler_view?.layoutManager = linearLayoutManager
    }

    override fun setUpPlaceDetails(localPlace: LocalPlace?) {
        supportActionBar?.title = localPlace?.name
        suffix_text_view?.text = localPlace?.name
        description_text_view?.text = localPlace?.description
        address_text_view?.text = localPlace?.address

        var items = localPlace?.photos.orEmpty().map { PictureItem(it) }
        if (items.isEmpty()) {
            items += PictureItem(localPlace?.thumbnailUrl)
        }
        fastAdapter.add(items)

        learn_more_button.clicks().subscribe({
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(localPlace?.url))
            startActivity(browserIntent)
        }, ::e)
    }
}
