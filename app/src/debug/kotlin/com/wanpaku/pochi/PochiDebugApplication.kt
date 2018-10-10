package com.wanpaku.pochi

import android.content.Context
import android.support.multidex.MultiDex

class PochiDebugApplication : PochiApplication() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

}