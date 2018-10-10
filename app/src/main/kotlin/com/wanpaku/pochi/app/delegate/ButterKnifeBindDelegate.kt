package com.wanpaku.pochi.app.delegate

import android.view.View
import butterknife.ButterKnife
import butterknife.Unbinder
import com.wanpaku.pochi.app.internal.BaseFragment
import com.wanpaku.pochi.app.internal.ViewLifecycle

class ButterKnifeBindDelegate(private val target: Any)
    : ViewLifecycle {

    private var unbinder: Unbinder? = null

    override fun onViewCreated(fragment: BaseFragment, source: View) {
        unbinder = ButterKnife.bind(target, source)
    }

    override fun onDestroyView(fragment: BaseFragment) {
        unbinder?.unbind()
    }

}
