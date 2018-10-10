package com.wanpaku.pochi.app.sign_up.presenters

import android.content.Context
import android.view.View
import com.facebook.AccessToken
import com.wanpaku.pochi.app.internal.BaseFragment
import com.wanpaku.pochi.app.internal.ViewLifecycle
import com.wanpaku.pochi.app.service.UserService
import com.wanpaku.pochi.app.sign_up.SignUpError
import com.wanpaku.pochi.app.sign_up.view_models.SignUpFormData
import com.wanpaku.pochi.domain.user.User
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class SignUpFormPresenter @Inject constructor(
        private val context: Context,
        private val userService: UserService,
        private val disposables: CompositeDisposable)
    : ViewLifecycle, SignUpFormContract.Presenter {

    private val TAG = SignUpFormPresenter::class.java.simpleName

    private var view: SignUpFormContract.View? = null

    override fun createUserWithEmailAndPassword(formData: SignUpFormData) {
        val email = formData.email ?: return
        val password = formData.password ?: return
        createUser(userService.createUserEmailAndPassword(email, password, formData))
    }

    override fun createUserWithFacebook(accessToken: AccessToken, formData: SignUpFormData) {
        createUser(userService.createUserFacebook(accessToken, formData))
    }

    override fun onViewCreated(fragment: BaseFragment, source: View) {
        view = fragment as? SignUpFormContract.View
    }

    override fun onDestroyView(fragment: BaseFragment) {
        view = null
        disposables.clear()
    }

    private fun createUser(create: Single<User>) {
        view?.setIndicator(true)
        disposables.add(create.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { view?.setIndicator(false) }
                .subscribe({
                    view?.launchDogRegistration()
                }, {
                    Timber.tag(TAG).e(it, "failed to signup")
                    val message = context.getString(SignUpError.from(it).messageResId)
                    view?.showError(message)
                }))
    }


}