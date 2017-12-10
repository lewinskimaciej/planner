package com.atc.planner.presentation.settings

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.view.MenuItem
import com.atc.planner.R
import com.atc.planner.data.repository.places_repository.SightsFilterDetails
import com.atc.planner.extension.orFalse
import com.atc.planner.extension.orZero
import com.atc.planner.extension.setupToolbarWithUpNavigation
import com.atc.planner.presentation.base.BaseMvpActivity
import com.github.ajalt.timberkt.e
import com.jakewharton.rxbinding2.widget.checkedChanges
import com.jakewharton.rxbinding2.widget.textChanges
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.content_settings.*
import javax.inject.Inject


class SettingsActivity : BaseMvpActivity<SettingsView, SettingsPresenter>(), SettingsView {

    @Inject
    lateinit var settingsPresenter: SettingsPresenter

    override fun createPresenter(): SettingsPresenter = settingsPresenter

    override val layoutResId: Int?
        get() = R.layout.activity_settings

    companion object {
        const val CHANGED_KEY = "changed"
        fun start(from: FragmentActivity) {
            val intent = Intent(from, SettingsActivity::class.java)
            from.startActivityForResult(intent, 1337)
        }
    }

    override fun onViewCreated(savedInstanceState: Bundle?) {
        setupToolbarWithUpNavigation(toolbar)

        bindViews()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> {
            presenter?.end()
            true
        }
        else -> true
    }

    override fun onBackPressed() {
        presenter?.end()
    }

    override fun endWithResult(changed: Boolean) {
        val intent = Intent()
        val bundle = Bundle()
        bundle.putBoolean(CHANGED_KEY, changed)
        intent.putExtras(bundle)
        setResult(1337, intent)
        this.finish()
    }

    private fun bindViews() {
        i_am_a_child_checkbox.checkedChanges()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ presenter?.onIAmAChildCheckboxChanges(it) }, ::e)

        museums_checkbox.checkedChanges()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ presenter?.onMuseumCheckboxChanges(it) }, ::e)

        art_galleries_checkbox.checkedChanges()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ presenter?.onArtGalleriesCheckboxChanges(it) }, ::e)

        physical_activity_checkbox.checkedChanges()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ presenter?.onPhysicalActivityCheckboxChanges(it) }, ::e)

        max_entry_fee_edit_text.textChanges()
                .skipInitialValue()
                .map {
                    max_entry_fee_edit_text.rawValue.toFloat() / 100
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ presenter?.onMaxEntryFeeChanges(it) }, ::e)
    }

    override fun setUpValues(filterDetails: SightsFilterDetails?) {
        i_am_a_child_checkbox.isChecked = filterDetails?.targetsChildren.orFalse()
        museums_checkbox.isChecked = filterDetails?.canBeAMuseum.orFalse()
        art_galleries_checkbox.isChecked = filterDetails?.canBeAnArtGallery.orFalse()
        physical_activity_checkbox.isChecked = filterDetails?.canBePhysicalActivity.orFalse()
        max_entry_fee_edit_text.setValue(filterDetails?.maxEntryFee.orZero().toLong() * 100)
    }
}
