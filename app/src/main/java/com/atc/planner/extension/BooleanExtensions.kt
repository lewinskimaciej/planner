package com.atc.planner.extension

fun Boolean?.orFalse() = this ?: false

fun Boolean?.orTrue() = this ?: true
