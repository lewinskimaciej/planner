package com.atc.planner.presentation.main

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSnapHelper
import com.atc.planner.R
import com.atc.planner.extensions.orZero
import com.atc.planner.extensions.visible
import com.atc.planner.presentation.base.BaseMvpActivity
import com.atc.planner.presentation.main.adapter.PlaceItem
import com.github.ajalt.timberkt.e
import com.jakewharton.rxbinding2.view.clicks
import com.tbruyelle.rxpermissions2.RxPermissions
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import javax.inject.Inject


class MainActivity : BaseMvpActivity<MainView, MainPresenter>(), MainView {

    @Inject
    lateinit var mainPresenter: MainPresenter

    override fun createPresenter(): MainPresenter = mainPresenter

    override val layoutResId: Int?
        get() = R.layout.activity_main

    private lateinit var rxPermissions: RxPermissions

    private var adapter: FlexibleAdapter<IFlexible<*>>? = null
    private var linearLayoutManager: LinearLayoutManager? = null

    override fun onViewCreated(savedInstanceState: Bundle?) {
        setSupportActionBar(toolbar)
        rxPermissions = RxPermissions(this)

        linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        adapter = FlexibleAdapter(null)

        main_recycler_view.layoutManager = linearLayoutManager
        main_recycler_view.adapter = adapter

        val linearSnapHelper = LinearSnapHelper()
        linearSnapHelper.attachToRecyclerView(main_recycler_view)

        bindViews()
    }

    private fun bindViews() {
        main_arrow_before.clicks().subscribe({
            var position = linearLayoutManager?.findFirstVisibleItemPosition().orZero()
            if (position > 0) {
                position--
            }
            main_recycler_view?.smoothScrollToPosition(position)
        }, ::e)

        main_arrow_next.clicks().subscribe({
            var position = linearLayoutManager?.findFirstVisibleItemPosition().orZero()
            if (position < adapter?.itemCount.orZero()) {
                position++
            }
            main_recycler_view?.smoothScrollToPosition(position)
        }, ::e)
    }

    override fun askForLocationPermission() {
        rxPermissions.request(android.Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe({
                    if (it) {
                        presenter?.onPermissionsGranted()
                    } else {
                        presenter?.onPermissionsRefused()
                    }
                }, ::e)
    }

    override fun setItems(items: List<PlaceItem>) {
        adapter?.updateDataSet(items)
        main_first_row_container.visible()
    }

    override fun addItems(items: List<PlaceItem>) {
        adapter?.onLoadMoreComplete(items)
    }
}
