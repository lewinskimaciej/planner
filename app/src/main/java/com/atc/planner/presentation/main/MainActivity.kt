package com.atc.planner.presentation.main

import android.Manifest
import android.os.Bundle
import com.atc.planner.R
import com.atc.planner.presentation.base.BaseMvpActivity
import com.atc.planner.presentation.map.MapFragment
import com.github.jksiezni.permissive.Permissive
import javax.inject.Inject


class MainActivity : BaseMvpActivity<MainView, MainPresenter>(), MainView {

    @Inject
    lateinit var mainPresenter: MainPresenter

    override fun createPresenter(): MainPresenter = mainPresenter

    override val layoutResId: Int?
        get() = R.layout.activity_main

    override fun onViewCreated(savedInstanceState: Bundle?) {
        val mapFragment = MapFragment()
        supportFragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, mapFragment)
                .commitAllowingStateLoss()
    }

    override fun askForLocationPermission() {
        Permissive.Request(Manifest.permission.ACCESS_FINE_LOCATION)
                .whenPermissionsGranted {
                    presenter?.onPermissionsGranted()
                }
                .whenPermissionsRefused {
                    presenter?.onPermissionsRefused()
                }
                .execute(this)
    }

}