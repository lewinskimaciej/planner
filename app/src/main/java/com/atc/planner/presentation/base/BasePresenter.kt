package com.atc.planner.presentation.base

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter
import com.hannesdorfmann.mosby3.mvp.MvpView
import java.io.Serializable


abstract class BasePresenter<V : MvpView> : MvpBasePresenter<V>() {

    abstract fun onViewCreated(data: Serializable?)

}
