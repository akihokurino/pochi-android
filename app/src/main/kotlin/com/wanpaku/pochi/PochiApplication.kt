package com.wanpaku.pochi

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.wanpaku.pochi.infra.di.ApplicationComponent
import com.wanpaku.pochi.infra.di.DaggerApplicationComponent
import com.wanpaku.pochi.infra.di.InfraModule
import io.fabric.sdk.android.Fabric
import net.danlew.android.joda.JodaTimeAndroid
import timber.log.Timber

open class PochiApplication : Application() {

    val component: ApplicationComponent by lazy {
        DaggerApplicationComponent.builder()
                .infraModule(InfraModule(this))
                .build()
    }

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
        Fabric.with(this, Crashlytics())
        JodaTimeAndroid.init(this)
    }

}