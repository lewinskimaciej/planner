package com.atc.planner.extensions

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import kotlin.reflect.KClass

/**
 * Starts specified activity.
 *
 * @param to   type of activity which will be started.
 */
fun Context.startActivity(to: KClass<*>) {
    val intent = Intent(this, to.java)
    this.startActivity(intent)
}

/**
 * Starts an activity with flags:
 *
 * [Intent.FLAG_ACTIVITY_SINGLE_TOP]
 * [Intent.FLAG_ACTIVITY_NO_HISTORY]
 * [Intent.FLAG_ACTIVITY_NEW_TASK]
 * [Intent.FLAG_ACTIVITY_CLEAR_TASK]
 *
 * @param to   type of activity which will be started.
 */
fun Context.startActivityWithNoHistory(to: KClass<*>) {
    val intent = Intent(this, to.java)
    intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    this.startActivity(intent)
}

/**
 * Starts specified activity.
 *
 * @param to     type of activity which will be started.
 * @param bundle bundle with data.
 */
fun Context.startActivity(to: KClass<*>, bundle: Bundle) {
    val intent = Intent(this, to.java)
    intent.putExtras(bundle)
    this.startActivity(intent)
}

/**
 * Starts an activity with flags:
 *
 * [Intent.FLAG_ACTIVITY_SINGLE_TOP]
 * [Intent.FLAG_ACTIVITY_NO_HISTORY]
 * [Intent.FLAG_ACTIVITY_NEW_TASK]
 * [Intent.FLAG_ACTIVITY_CLEAR_TASK]
 *
 * @param to     type of activity which will be started.
 * @param bundle bundle
 */
fun Context.startActivityWithNoHistory(to: KClass<*>, bundle: Bundle) {
    val intent = Intent(this, to.java)
    intent.putExtras(bundle)
    intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    this.startActivity(intent)
}

/**
 * Starts an activity with flags:
 *
 * @param to     type of activity which will be started.
 * @param bundle bundle
 * @param options Additional options for how the Activity should be started.
 * May be null if there are no options. See android.app.ActivityOptions for how to build the
 * Bundle supplied here; there are no supported definitions for building it manually.
 */
fun Context.startActivity(to: KClass<*>, bundle: Bundle? = null, options: Bundle? = null) {
    val intent = Intent(this, to.java)
    if (bundle != null) {
        intent.putExtras(bundle)
    }
    if (options != null) {
        this.startActivity(intent, options)
    } else {
        this.startActivity(intent)
    }
}

/**
 * Starts an activity with flags:
 *
 *
 * [Intent.FLAG_ACTIVITY_SINGLE_TOP]
 * [Intent.FLAG_ACTIVITY_NO_HISTORY]
 * [Intent.FLAG_ACTIVITY_NEW_TASK]
 * [Intent.FLAG_ACTIVITY_CLEAR_TASK]
 *
 * @param to     type of activity which will be started.
 * @param bundle bundle
 * @param options Additional options for how the Activity should be started.
 * May be null if there are no options. See android.app.ActivityOptions for how to build the
 * Bundle supplied here; there are no supported definitions for building it manually.
 */
fun Context.startActivityWithNoHistory(to: KClass<*>, bundle: Bundle?, options: Bundle?) {
    val intent = Intent(this, to.java)
    intent.putExtras(bundle)
    intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    this.startActivity(intent, options)
}

/**
 * Sets toolbar with up navigation.
 *
 * @param toolbar  [Toolbar]
 */
fun AppCompatActivity.setupToolbarWithUpNavigation(toolbar: Toolbar) {
    try {
        this.setSupportActionBar(toolbar)
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        this.supportActionBar?.setDisplayShowHomeEnabled(true)
    } catch (e: Exception) {
        Log.e(this.localClassName, e.message)
    }

}

fun FragmentActivity.inputMethodService(): InputMethodManager {
    return this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
}

fun FragmentActivity.showSoftInput(view: View, flags: Int = InputMethodManager.SHOW_IMPLICIT) {
    if (view.requestFocus()) {
        view.postDelayed({
            this.inputMethodService().showSoftInput(view, flags)
        }, 100)
    }
}

fun FragmentActivity.hideSoftInput(flags: Int = 0) {
    this.inputMethodService().hideSoftInputFromWindow(this.currentFocus?.windowToken, flags)
}
