package com.wanpaku.pochi.infra.di

import javax.inject.Scope
import kotlin.annotation.Retention

@Scope @Retention(AnnotationRetention.RUNTIME)
annotation class PerFragment

@Scope @Retention(AnnotationRetention.RUNTIME)
annotation class PerActivity

@Scope @Retention(AnnotationRetention.RUNTIME)
annotation class PerApplication