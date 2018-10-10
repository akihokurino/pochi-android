package com.wanpaku.pochi.infra.di

import com.wanpaku.pochi.PochiApplication
import dagger.Component

@PerApplication
@Component(modules = arrayOf(InfraModule::class))
interface ApplicationComponent {
    fun inject(application: PochiApplication)
    fun defaultActivityComponent(module: ActivityModule): DefaultActivityComponent
    fun defaultFragmentComponent(): DefaultFragmentComponent
}