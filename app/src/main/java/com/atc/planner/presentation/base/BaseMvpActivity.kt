package com.atc.planner.presentation.base

import android.content.Intent
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.ViewGroup
import android.widget.Toast
import com.atc.planner.R
import com.hannesdorfmann.mosby3.mvp.MvpActivity
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasFragmentInjector
import dagger.android.support.HasSupportFragmentInjector
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

abstract class BaseMvpActivity<V : BaseView, P : BaseMvpPresenter<V>> : MvpActivity<V, P>(),
        HasFragmentInjector,
        HasSupportFragmentInjector,
        BaseView {

    @get:LayoutRes
    protected abstract val layoutResId: Int?

    private var disposables = CompositeDisposable()

    @Inject
    lateinit var supportFragmentInjector: DispatchingAndroidInjector<Fragment>
    @Inject
    lateinit var frameworkFragmentInjector: DispatchingAndroidInjector<android.app.Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        if (layoutResId != null) {
            setContentView(layoutResId as Int)
        }

        onViewCreated(savedInstanceState)
        presenter?.onViewCreated(intent.extras?.getSerializable(BaseDictionary.KEY_SERIALIZABLE))

    }

    override fun onNewIntent(intent: Intent) {
        val extras = intent.extras?.getSerializable(BaseDictionary.KEY_SERIALIZABLE)
        presenter?.onNewBundle(extras)

        super.onNewIntent(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

    override fun fragmentInjector(): AndroidInjector<android.app.Fragment> = frameworkFragmentInjector

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = supportFragmentInjector

    override fun showAlertDialog(title: String, message: String) {
        AlertDialog.Builder(this, R.style.AppTheme)
                .setMessage(message)
                .setTitle(title)
                .setPositiveButton(R.string.common_dialog_ok, { dialog, _ ->
                    dialog.dismiss()
                })
    }

    override fun showErrorToast() {
        Toast.makeText(this, getString(R.string.unexpected_error), Toast.LENGTH_SHORT).show()
    }

    override fun showOfflineSnackbar() {
        val rootView = this.findViewById<ViewGroup>(android.R.id.content).getChildAt(0)
        Snackbar.make(rootView, getString(R.string.no_internet_connection), Snackbar.LENGTH_LONG)
    }
}