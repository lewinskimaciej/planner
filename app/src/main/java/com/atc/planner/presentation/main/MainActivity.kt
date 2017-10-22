package com.atc.planner.presentation.main

import android.os.Bundle
import android.support.v4.app.Fragment
import com.atc.planner.R
import com.atc.planner.data.models.local.LocalPlace
import com.atc.planner.presentation.base.BaseMvpActivity
import com.atc.planner.presentation.map.MapFragment
import com.atc.planner.presentation.map.MapView
import com.github.ajalt.timberkt.e
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : BaseMvpActivity<MainView, MainPresenter>(), MainView {

    @Inject
    lateinit var mainPresenter: MainPresenter

    override fun createPresenter(): MainPresenter = mainPresenter

    override val layoutResId: Int?
        get() = R.layout.activity_main

    private lateinit var rxPermissions: RxPermissions

    private var mapFragment: Fragment? = MapFragment()

    override fun onViewCreated(savedInstanceState: Bundle?) {
        setSupportActionBar(toolbar)
        rxPermissions = RxPermissions(this)

        supportFragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, mapFragment)
                .commitAllowingStateLoss()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.onDestroy()
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

    override fun setItems(items: List<LocalPlace>) {
        // set markers on map
        (mapFragment as? MapView)?.setData(items)
    }

    override fun addItems(items: List<LocalPlace>) {
        // add markers to map
        (mapFragment as? MapView)?.addData(items)
    }
}
