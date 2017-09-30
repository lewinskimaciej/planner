package com.atc.planner.presentation.base

import android.content.Context
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.atc.planner.R
import com.github.ajalt.timberkt.d
import com.hannesdorfmann.mosby3.mvp.MvpFragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import io.reactivex.disposables.CompositeDisposable
import java.io.Serializable
import javax.inject.Inject

abstract class BaseMvpFragment<V : BaseView, P : BasePresenter<V>> : MvpFragment<V, P>(),
        BaseView,
        HasSupportFragmentInjector {

    @get:LayoutRes
    protected abstract val layoutResId: Int

    @Inject
    lateinit var childFragmentInjector: DispatchingAndroidInjector<Fragment>

    private var disposables = CompositeDisposable()

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = childFragmentInjector

    fun setSerializableArgument(serializable: Serializable? = null): Fragment {
        val bundle = Bundle()
        serializable?.let { bundle.putSerializable(BaseDictionary.KEY_SERIALIZABLE, it) }
        this.arguments = bundle
        return this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(layoutResId, container, false)
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        d { "onViewCreated" }
        onViewCreated(savedInstanceState)
        presenter?.onViewCreated(arguments?.getSerializable(BaseDictionary.KEY_SERIALIZABLE))
    }

    override fun onAttach(context: Context?) {
        d { "onAttach" }
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

    override fun showAlertDialog(title: String, message: String) {
        AlertDialog.Builder(activity, R.style.AppTheme)
                .setMessage(message)
                .setTitle(title)
                .setPositiveButton(R.string.common_dialog_ok, { dialog, _ ->
                    dialog.dismiss()
                })
    }

    override fun showErrorToast() {
        Toast.makeText(activity, getString(R.string.unexpected_error), Toast.LENGTH_SHORT).show()
    }

    override fun showOfflineSnackbar() {
        val rootView = activity.findViewById<ViewGroup>(android.R.id.content).getChildAt(0)
        Snackbar.make(rootView, getString(R.string.no_internet_connection), Snackbar.LENGTH_LONG)
    }
}
