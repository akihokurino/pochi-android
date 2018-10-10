package com.wanpaku.pochi.app.delegate

import com.wanpaku.pochi.app.internal.BaseFragment
import com.wanpaku.pochi.app.internal.ViewLifecycle
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

interface CompositeDisposables {

    fun add(disposable: Disposable)

    fun clear()

}

class DisposableDelegate : CompositeDisposables {

    private val disposables: CompositeDisposable = CompositeDisposable()

    override fun add(disposable: Disposable) {
        disposables.add(disposable)
    }

    override fun clear() {
        disposables.clear()
    }

}