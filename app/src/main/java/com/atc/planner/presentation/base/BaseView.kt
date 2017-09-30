package com.atc.planner.presentation.base

import android.os.Bundle
import com.hannesdorfmann.mosby3.mvp.MvpView

interface BaseView : MvpView {
    fun showErrorToast()
    fun showOfflineSnackbar()
    fun onViewCreated(savedInstanceState: Bundle?)
    fun showAlertDialog(title: String, message: String)
}
