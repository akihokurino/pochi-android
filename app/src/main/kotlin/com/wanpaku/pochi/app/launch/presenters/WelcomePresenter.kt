package com.wanpaku.pochi.app.launch.presenters

import android.net.Uri
import android.view.View
import com.facebook.AccessToken
import com.wanpaku.pochi.app.internal.BaseFragment
import com.wanpaku.pochi.app.internal.ViewLifecycle
import com.wanpaku.pochi.infra.facebook.FacebookAuth
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class WelcomePresenter @Inject constructor(
        private val disposables: CompositeDisposable)
    : WelcomeContract.Presenter, ViewLifecycle {

    private var view: WelcomeContract.View? = null

    override fun selectTextLink(title: String, url: String) {
        view?.launchWebView(title, Uri.parse(url))
    }

    override fun onLoggedInFacebook(accessToken: AccessToken) {
        disposables.add(FacebookAuth.profileWithTracking()
                .subscribe { profile ->
                    view?.launchSignUpForFacebook(accessToken, profile)
                })
    }

    override fun onViewCreated(fragment: BaseFragment, source: View) {
        view = fragment as? WelcomeContract.View
    }

    override fun onDestroyView(fragment: BaseFragment) {
        view = null
        disposables.clear()
    }

}