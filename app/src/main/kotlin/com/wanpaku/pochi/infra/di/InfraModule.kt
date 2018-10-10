package com.wanpaku.pochi.infra.di

import android.content.Context
import com.google.gson.Gson
import com.securepreferences.SecurePreferences
import com.wanpaku.pochi.PochiApplication
import com.wanpaku.pochi.R
import com.wanpaku.pochi.infra.api.ApiRequestInterceptor
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import okhttp3.Cache
import okhttp3.ConnectionPool
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Named

@Module
class InfraModule(private val applicationContext: PochiApplication) {

    @PerApplication
    @Provides
    fun provideApplicationContext(): Context {
        return applicationContext
    }

    @PerApplication
    @Provides
    fun provideSecurePreference(): SecurePreferences {
        return SecurePreferences(applicationContext)
    }

    @PerApplication
    @Provides
    fun provideOkHttpClient(apiInterceptor: ApiRequestInterceptor): OkHttpClient {
        val httpCacheDirectory = File(applicationContext.cacheDir, "responses")
        val cacheSize: Long = 10 * 1024 * 1024 // 10 MiB
        val cache = Cache(httpCacheDirectory, cacheSize)

        val builder = OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .connectionPool(ConnectionPool(10, 5, TimeUnit.MINUTES))
                .addInterceptor(apiInterceptor)
                .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
                .cache(cache)

        return builder.build()
    }

    @Provides
    @Named("ApiServerUrl")
    fun provideApiServerUrl(securePreferences: SecurePreferences): String {
        return applicationContext.resources.getString(R.string.production_server_url)
    }

    @PerApplication
    @Provides
    fun provideForRetrofitBaseHttpUrl(@Named("ApiServerUrl") url: String): HttpUrl {
        return HttpUrl.parse(url)!!
    }

    @PerApplication
    @Provides
    fun provideGson() = Gson()

    @Provides
    fun provideCompositeDisposable() = CompositeDisposable()

}