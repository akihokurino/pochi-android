package com.wanpaku.pochi.app.task

import com.wanpaku.pochi.app.internal.BaseFragment
import com.wanpaku.pochi.app.internal.ViewLifecycle

class TaskFragment : BaseFragment() {

    companion object {

        val TAG = TaskFragment::class.java.simpleName

        fun newInstance() = TaskFragment()

    }

    override fun viewLifecycle(): List<ViewLifecycle> = emptyList()

}