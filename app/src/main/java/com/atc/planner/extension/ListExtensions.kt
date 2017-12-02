package com.atc.planner.extension

fun ArrayList<Any>?.orEmpty(): ArrayList<Any> = this ?: ArrayList()
