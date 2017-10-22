package com.atc.planner.presentation.base

import java.io.Serializable

interface BasePresenter {
    fun onNewBundle(data: Serializable?) {}
}