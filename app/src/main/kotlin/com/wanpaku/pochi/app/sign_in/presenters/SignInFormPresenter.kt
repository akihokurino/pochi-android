package com.wanpaku.pochi.app.sign_in.presenters

import android.content.Context
import android.view.View
import com.facebook.AccessToken
import com.wanpaku.pochi.app.internal.BaseFragment
import com.wanpaku.pochi.app.internal.ViewLifecycle
import com.wanpaku.pochi.app.service.UserService
import com.wanpaku.pochi.app.sign_in.SignInError
import com.wanpaku.pochi.app.sign_in.view_models.SignInFormData
import com.wanpaku.pochi.domain.user.User
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class SignInFormPresenter @Inject constructor(
        private val context: Context,
        private val userService: UserService,
        private val disposables: CompositeDisposable)
    : ViewLifecycle, SignInContract.Presenter {

    private val TAG = SignInFormPresenter::class.java.simpleName

    private var view: SignInContract.View? = null

    override fun loginEmailAndPassword(formData: SignInFormData) {
        login(userService.loginEmailAndPassword(formData))
    }

    override fun loginFacebook(accessToken: AccessToken) {
        login(userService.loginFacebook(accessToken))
    }

    override fun onViewCreated(fragment: BaseFragment, source: View) {
        view = fragment as? SignInContract.View
    }

    override fun onDestroyView(fragment: BaseFragment) {
        view = null
        disposables.clear()
    }

    private fun login(login: Single<User>) {
        view?.setIndicator(true)
        disposables.add(login.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { view?.setIndicator(false) }
                .subscribe({
                    view?.launchSearch()
                }, {
                    val message = context.getString(SignInError.from(it).messageResId)
                    view?.showSignInError(message)
                    Timber.tag(TAG).e(it, "failed to sign in.")
                }))
    }

}