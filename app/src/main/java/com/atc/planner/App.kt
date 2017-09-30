package com.atc.planner

import com.atc.planner.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import de.javakaffee.kryoserializers.jodatime.JodaDateTimeSerializer
import io.paperdb.Paper
import org.joda.time.DateTime


class App : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> = DaggerAppComponent.builder().create(this)

    override fun onCreate() {
        super.onCreate()

        Paper.addSerializer(DateTime::class.java, JodaDateTimeSerializer())
        Paper.init(this)
    }
}