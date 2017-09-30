package com.atc.planner.extensions

fun ArrayList<Any>?.orEmpty(): ArrayList<Any> = this ?: ArrayList()
