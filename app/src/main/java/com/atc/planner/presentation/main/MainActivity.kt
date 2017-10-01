package com.atc.planner.presentation.main

import android.os.Bundle
import com.atc.planner.R
import com.atc.planner.presentation.base.BaseMvpActivity
import com.atc.planner.presentation.map.MapFragment
import com.github.ajalt.timberkt.e
import com.tbruyelle.rxpermissions2.RxPermissions
import java.util.jar.Manifest
import javax.inject.Inject


class MainActivity : BaseMvpActivity<MainView, MainPresenter>(), MainView {

    @Inject
    lateinit var mainPresenter: MainPresenter

    override fun createPresenter(): MainPresenter = mainPresenter

    override val layoutResId: Int?
        get() = R.layout.activity_main

    lateinit var rxPermissions: RxPermissions

    override fun onViewCreated(savedInstanceState: Bundle?) {
        rxPermissions = RxPermissions(this)

        val mapFragment = MapFragment()
        supportFragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, mapFragment)
                .commitAllowingStateLoss()
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

}