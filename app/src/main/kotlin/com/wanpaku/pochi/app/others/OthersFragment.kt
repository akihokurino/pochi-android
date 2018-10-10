package com.wanpaku.pochi.app.others

import com.wanpaku.pochi.app.internal.BaseFragment
import com.wanpaku.pochi.app.internal.ViewLifecycle

class OthersFragment : BaseFragment() {

    companion object {

        val TAG = OthersFragment::class.java.simpleName

        fun newInstance() = OthersFragment()

    }

    override fun viewLifecycle(): List<ViewLifecycle> = emptyList()

}