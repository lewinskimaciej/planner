package com.atc.planner.di.modules

import android.app.Application
import com.atc.planner.App
import dagger.Binds
import dagger.Module

@Module
abstract class AppModule {

    @Binds
    abstract fun application(app: App): Application
}
