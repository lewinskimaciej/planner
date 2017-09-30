package com.atc.planner.di.modules

import com.atc.planner.di.scopes.ActivityScope
import com.atc.planner.presentation.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun mainActivity(): MainActivity
}