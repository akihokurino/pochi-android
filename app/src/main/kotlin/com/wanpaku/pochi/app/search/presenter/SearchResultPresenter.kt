package com.wanpaku.pochi.app.search.presenter

import android.view.View
import com.wanpaku.pochi.app.internal.BaseFragment
import com.wanpaku.pochi.app.internal.ViewLifecycle
import com.wanpaku.pochi.app.search.contract.SearchResultContract
import com.wanpaku.pochi.domain.ZipCode
import com.wanpaku.pochi.domain.repositories.SitterRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class SearchResultPresenter @Inject constructor(
        private val sitterRepository: SitterRepository,
        private val disposables: CompositeDisposable)
    : SearchResultContract.Presenter, ViewLifecycle {

    private val TAG = SearchResultPresenter::class.java.simpleName

    private var view: SearchResultContract.View? = null

    override fun fetchSitters(zipCode: ZipCode) {
        view?.setIndicator(true)
        disposables.add(sitterRepository.fetchSitters(zipCode = zipCode.zipCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { view?.setIndicator(false) }
                .subscribe({
                    view?.updateList(it.second)
                }, {
                    view?.showError()
                    Timber.tag(TAG).e(it, "failed to fetch sitters. zipCode is %s", zipCode.zipCode)
                }))
    }

    override fun onViewCreated(fragment: BaseFragment, source: View) {
        view = fragment as? SearchResultContract.View
    }

    override fun onDestroyView(fragment: BaseFragment) {
        view = null
        disposables.clear()
    }

}
