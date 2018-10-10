package com.wanpaku.pochi.infra.di

import android.support.v7.app.AppCompatActivity
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(val activity: AppCompatActivity) {

    @PerActivity
    @Provides
    fun provideAppCompatActivity(): AppCompatActivity {
        return activity
    }
}

