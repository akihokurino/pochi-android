package com.wanpaku.pochi.app.internal

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.wanpaku.pochi.PochiApplication
import com.wanpaku.pochi.infra.di.DefaultFragmentComponent

abstract class BaseFragment : Fragment() {

    protected val baseActivity by lazy { activity as BaseActivity }

    protected val component: DefaultFragmentComponent by lazy {
        (context.applicationContext as PochiApplication).component
                .defaultFragmentComponent()
    }

    private val viewLifecycle: List<ViewLifecycle> by lazy {
        viewLifecycle()
    }

    protected abstract fun viewLifecycle(): List<ViewLifecycle>

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        viewLifecycle.forEach { it.saveInstanceState(this, outState) }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        viewLifecycle.forEach { it.restored(this, savedInstanceState) }
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view ?: return
        viewLifecycle.forEach { it.onViewCreated(this, view) }
    }

    override fun onResume() {
        super.onResume()
        viewLifecycle.forEach { it.onResume(this) }
    }

    override fun onPause() {
        super.onPause()
        viewLifecycle.forEach { it.onPause(this) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewLifecycle.forEach { it.onDestroyView(this) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        viewLifecycle.forEach { it.onActivityResult(this, requestCode, resultCode, data) }
    }
}