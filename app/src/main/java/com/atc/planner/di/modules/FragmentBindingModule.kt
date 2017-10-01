package com.atc.planner.di.modules

import com.atc.planner.di.scopes.FragmentScope
import com.atc.planner.presentation.map.MapFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBindingModule {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun mapFragment(): MapFragment
}