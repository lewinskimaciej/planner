package com.atc.planner.di.modules


import android.app.Application
import android.content.Context
import com.atc.planner.R
import com.atc.planner.commons.*
import com.atc.planner.data.api.CustomInterceptor
import com.atc.planner.di.qualifiers.NullOnEmptyConverterFactory
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import org.joda.time.DateTime
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class CommonModule {

    companion object {
        private val DEFAULT_TIMEOUT = 30
    }

    @Provides
    @Singleton
    fun application(application: Application): Context = application

    @Provides
    @Singleton
    fun okHttpClient(context: Application): OkHttpClient = OkHttpClient.Builder()
            .readTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .connectTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .addInterceptor(CustomInterceptor.loggingInterceptor())
            .build()

    @Provides
    @Singleton
    fun retrofit(context: Application,
                 okHttpClient: OkHttpClient,
                 gson: Gson,
                 @NullOnEmptyConverterFactory nullOnEmptyConverterFactory: Converter.Factory): Retrofit {
        val baseUrl = context.getString(R.string.api_path)

        return Retrofit.Builder()
                .addConverterFactory(nullOnEmptyConverterFactory)
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(
                        GsonConverterFactory.create(gson)
                )
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }


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
}
