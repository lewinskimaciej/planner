package com.atc.planner.presentation.main

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Menu
import android.view.MenuItem
import com.atc.planner.R
import com.atc.planner.data.model.local.Place
import com.atc.planner.extension.gone
import com.atc.planner.extension.visible
import com.atc.planner.presentation.base.BaseMvpActivity
import com.atc.planner.presentation.map.MapFragment
import com.atc.planner.presentation.map.MapView
import com.atc.planner.presentation.settings.SettingsActivity
import com.github.ajalt.timberkt.d
import com.github.ajalt.timberkt.e
import com.google.android.gms.maps.model.LatLng
import com.tbruyelle.rxpermissions2.RxPermissions
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

    private var mapFragment: Fragment? = MapFragment()

    override fun onViewCreated(savedInstanceState: Bundle?) {
        setSupportActionBar(toolbar)
        rxPermissions = RxPermissions(this)

        supportFragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, mapFragment)
                .commitAllowingStateLoss()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1337) {
            val didChange = data?.extras?.getBoolean(SettingsActivity.CHANGED_KEY)
            d { "diChange: $didChange" }
            if (didChange == true) {
                presenter?.requestRefresh()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.settings -> {
            goToSettings()
            true
        }
        R.id.refresh -> {
            presenter?.requestRefresh()
            true
        }
        else -> true
    }

    private fun goToSettings() {
        SettingsActivity.start(this)
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

    override fun setItems(items: List<Place>) {
        // set markers on map
        (mapFragment as? MapView)?.setData(items)
    }

    override fun addItems(items: List<Place>) {
        // add markers to map
        (mapFragment as? MapView)?.addData(items)
    }

    override fun addPolyline(polyline: List<LatLng>) {
        (mapFragment as? MapView)?.drawPolyline(polyline)
    }

    override fun highlightMarker(place: Place?) {
        (mapFragment as? MapView)?.highlightMarker(place)
    }

    override fun clearPolyline() {
        (mapFragment as? MapView)?.clearPolyline()
    }

    override fun clearMarkers() {
        (mapFragment as? MapView)?.clearMarkers()
    }

    override fun setLoaderVisibility(visible: Boolean) {
        if (visible) {
            loader.visible()
        } else {
            loader.gone()
        }
    }
}
