package com.atc.planner.di

import com.atc.planner.App
import com.atc.planner.di.modules.ActivityBindingModule
import com.atc.planner.di.modules.AppModule
import com.atc.planner.di.modules.CommonModule
import com.atc.planner.di.modules.FragmentBindingModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class,
        CommonModule::class,

        ActivityBindingModule::class,
        FragmentBindingModule::class,

        AndroidSupportInjectionModule::class))
interface AppComponent : AndroidInjector<App> {
    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<App>()
}