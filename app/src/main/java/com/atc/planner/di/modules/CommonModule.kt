package com.atc.planner.di.modules


import android.app.Application
import android.content.Context
import com.atc.planner.App
import com.atc.planner.R
import com.atc.planner.commons.*
import com.atc.planner.di.qualifiers.NullOnEmptyConverterFactory
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.sygic.travel.sdk.StSDK
import dagger.Module
import dagger.Provides
import okhttp3.ResponseBody
import org.altbeacon.beacon.BeaconManager
import org.joda.time.DateTime
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type
import javax.inject.Singleton


@Module
class CommonModule {

    @Provides
    @Singleton
    fun application(application: Application): Context = application


    @Provides
    @Singleton
    fun gson(): Gson = GsonBuilder()
            .serializeNulls()
            .disableHtmlEscaping()
            .registerTypeAdapter(
                    DateTime::class.java,
                    DateTimeDeserializer()
            )
            .registerTypeAdapter(
                    DateTime::class.java,
                    DateTimeSerializer()
            ).create()

    @Provides
    @Singleton
    fun provideStringProvider(context: Context): StringProvider = StringProviderImpl(context)

    @Provides
    @Singleton
    fun provideColorProvider(context: Context): ColorProvider = ColorProviderImpl(context)

    @Provides
    @Singleton
    @NullOnEmptyConverterFactory
    fun nullOnEmptyConverterFactory() = object : Converter.Factory() {
        fun converterFactory() = this
        override fun responseBodyConverter(type: Type, annotations: Array<out Annotation>, retrofit: Retrofit) = object : Converter<ResponseBody, Any?> {
            val nextResponseBodyConverter = retrofit.nextResponseBodyConverter<Any?>(converterFactory(), type, annotations)
            override fun convert(value: ResponseBody) = if (value.contentLength() != 0L || value.contentLength() != -1L) nextResponseBodyConverter.convert(value) else null
        }
    }

    @Provides
    @Singleton
    fun locationProvider(app: App): LocationProvider = LocationProviderImpl(app)

    @Provides
    @Singleton
    fun sygicSdk(app: App): StSDK {
        StSDK.initialize(app.getString(R.string.sygic_api_key), app)
        return StSDK.getInstance()
    }

    @Provides
    fun bitmapProvider(app: App): BitmapProvider = BitmapProviderImpl(app)

    @Provides
    fun cityProvider(app: App): CityProvider = CityProviderImpl(app)

    @Provides
    @Singleton
    fun firestore(): FirebaseFirestore {
        val settings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build()

        val instance = FirebaseFirestore.getInstance()
        instance.firestoreSettings = settings

        return instance
    }

    @Provides
    @Singleton
    fun beaconManager(app: App): BeaconManager = BeaconManager.getInstanceForApplication(app)
}
