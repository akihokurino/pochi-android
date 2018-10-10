package com.wanpaku.pochi.app.search.presenter

import android.view.View
import com.wanpaku.pochi.app.internal.BaseFragment
import com.wanpaku.pochi.app.internal.ViewLifecycle
import com.wanpaku.pochi.app.search.contract.SearchContract
import com.wanpaku.pochi.domain.repositories.AppUserRepository
import com.wanpaku.pochi.domain.repositories.PagingSitterProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class SearchPresenter @Inject constructor(
        private val sitterProvider: PagingSitterProvider,
        private val userRepository: AppUserRepository,
        private val disposables: CompositeDisposable)
    : ViewLifecycle, SearchContract.Presenter {

    private val TAG = SearchPresenter::class.java.simpleName

    private var view: SearchContract.View? = null

    override fun fetchSitters() {
        view?.setIndicator(true)
        disposables.add(sitterProvider.next()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { view?.setIndicator(false) }
                .subscribe({
                    view?.updateList(it, userRepository.isLoggedIn())
                }, {
                    Timber.tag(TAG).e(it, "failed to fetch sitters")
                    // TODO
                    view?.showError("")
                }))
    }

    override fun onViewCreated(fragment: BaseFragment, source: View) {
        view = fragment as? SearchContract.View
    }

    override fun onDestroyView(fragment: BaseFragment) {
        view = null
        disposables.clear()
    }

}