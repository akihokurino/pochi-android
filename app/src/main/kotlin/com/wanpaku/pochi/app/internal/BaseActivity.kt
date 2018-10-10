package com.wanpaku.pochi.app.internal

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.wanpaku.pochi.PochiApplication
import com.wanpaku.pochi.infra.di.ActivityModule
import com.wanpaku.pochi.infra.di.DefaultActivityComponent

abstract class BaseActivity : AppCompatActivity() {

    protected val component: DefaultActivityComponent by lazy {
        (applicationContext as PochiApplication)
                .component
                .defaultActivityComponent(ActivityModule(this))
    }

    protected open fun lifecycleDelegates(): List<ActivityLifecycleDelegate> = emptyList()

    private val delegates by lazy { lifecycleDelegates() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        delegates.forEach { it.onCreate(this, savedInstanceState) }
    }

    override fun onStart() {
        super.onStart()
        delegates.forEach { it.onStart(this) }
    }

    override fun onResume() {
        super.onResume()
        delegates.forEach { it.onResume(this) }
    }

    override fun onPause() {
        super.onPause()
        delegates.forEach { it.onPause(this) }
    }

    override fun onDestroy() {
        super.onDestroy()
        delegates.forEach { it.onDestroy(this) }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        delegates.forEach { it.onNewIntent(this, intent) }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        delegates.forEach { it.onBackPressed(this) }
    }

    override fun onContentChanged() {
        super.onContentChanged()
        delegates.forEach { it.onContentChanged(this) }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return delegates.foldRight(false, { delegate, bool -> delegate.onOptionsItemSelected(this, item) || bool })
    }

}


interface ActivityLifecycleDelegate {

    fun onCreate(activity: BaseActivity, savedInstanceState: Bundle?) = Unit

    fun onStart(activity: BaseActivity) = Unit

    fun onResume(activity: BaseActivity) = Unit

    fun onPause(activity: BaseActivity) = Unit

    fun onDestroy(activity: BaseActivity) = Unit

    fun onNewIntent(activity: BaseActivity, intent: Intent?) = Unit

    fun onBackPressed(activity: BaseActivity) = false

    fun onContentChanged(activity: BaseActivity) = Unit

    fun onOptionsItemSelected(activity: BaseActivity, item: MenuItem) = false

}