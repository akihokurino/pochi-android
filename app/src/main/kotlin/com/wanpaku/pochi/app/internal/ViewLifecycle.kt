package com.wanpaku.pochi.app.internal

import android.content.Intent
import android.os.Bundle
import android.view.View

interface ViewLifecycle {
    fun onViewCreated(fragment: BaseFragment, source: View) = Unit
    fun restored(fragment: BaseFragment, saveInstanceState: Bundle?) = Unit
    fun onResume(fragment: BaseFragment) = Unit
    fun onPause(fragment: BaseFragment) = Unit
    fun saveInstanceState(fragment: BaseFragment, outState: Bundle?) = Unit
    fun onDestroyView(fragment: BaseFragment) = Unit
    fun onActivityResult(fragment: BaseFragment, requestCode: Int, resultCode: Int, data: Intent?) = Unit
}