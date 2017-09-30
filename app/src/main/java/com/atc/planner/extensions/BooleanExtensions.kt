package com.atc.planner.extensions

fun Boolean?.orFalse() = this ?: false

fun Boolean?.orTrue() = this ?: true
