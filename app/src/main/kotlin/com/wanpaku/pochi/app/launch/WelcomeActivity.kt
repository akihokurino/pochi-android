package com.wanpaku.pochi.app.launch

import com.wanpaku.pochi.app.internal.BaseActivity

class WelcomeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager
                .beginTransaction()
                .add(android.R.id.content, WelcomeFragment.newInstance())
                .commit()
    }
}
