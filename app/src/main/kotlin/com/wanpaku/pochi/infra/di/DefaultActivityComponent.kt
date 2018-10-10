package com.wanpaku.pochi.infra.di

import com.wanpaku.pochi.app.launch.LaunchActivity
import com.wanpaku.pochi.app.sign_up.SignUpFormActivity
import dagger.Subcomponent

@PerActivity
@Subcomponent(modules = arrayOf(ActivityModule::class))
interface DefaultActivityComponent {
    fun inject(activity: LaunchActivity)
    fun inject(activity: SignUpFormActivity)
}