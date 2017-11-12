package com.atc.planner.di.modules

import com.atc.planner.di.scopes.ActivityScope
import com.atc.planner.presentation.main.MainActivity
import com.atc.planner.presentation.place_details.PlaceDetailsActivity
import com.atc.planner.presentation.settings.SettingsActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun mainActivity(): MainActivity

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun placeDetailsActivity(): PlaceDetailsActivity


    @ActivityScope
    @ContributesAndroidInjector
    abstract fun settingsActivity(): SettingsActivity
}