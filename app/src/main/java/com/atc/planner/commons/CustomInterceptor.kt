package com.atc.planner.commons

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Configuration
import android.os.Build

import com.atc.planner.BuildConfig
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor

import java.util.Locale

import okhttp3.internal.platform.Platform

object CustomInterceptor {

    private fun getCurrentLocale(context: Context): Locale {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            getSystemLocale(context.resources.configuration)
        } else {
            getSystemLocaleLegacy(context.resources.configuration)
        }
    }

    @Suppress("DEPRECATION")
    private fun getSystemLocaleLegacy(config: Configuration): Locale {
        return config.locale
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun getSystemLocale(config: Configuration): Locale {
        return config.locales.get(0)
    }

    fun loggingInterceptor(): okhttp3.Interceptor {
        return LoggingInterceptor.Builder()
                .log(Platform.INFO)
                .request("HTTP Request")
                .response("HTTP Response")
                .addHeader("version", BuildConfig.VERSION_NAME)
                .setLevel(Level.BODY)
                .loggable(BuildConfig.DEBUG)
                .build()
    }
}
