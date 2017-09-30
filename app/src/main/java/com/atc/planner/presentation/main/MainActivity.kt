package com.atc.planner.presentation.main

import android.os.Bundle
import com.atc.planner.R
import com.atc.planner.presentation.base.BaseMvpActivity
import javax.inject.Inject

class MainActivity: BaseMvpActivity<MainView, MainPresenter>() {

    @Inject
    lateinit var mainPresenter: MainPresenter

    override fun createPresenter(): MainPresenter = mainPresenter

    override val layoutResId: Int?
        get() = R.layout.activity_main

    override fun onViewCreated(savedInstanceState: Bundle?) {

    }
}