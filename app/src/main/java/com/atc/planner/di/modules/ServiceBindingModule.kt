package com.atc.planner.di.modules

import com.atc.planner.data.service.SearchService
import com.atc.planner.di.scopes.ActivityScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ServiceBindingModule {
    @ActivityScope
    @ContributesAndroidInjector
    abstract fun searchService(): SearchService
}