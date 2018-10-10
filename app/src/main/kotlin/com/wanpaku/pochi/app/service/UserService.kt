package com.wanpaku.pochi.app.service

import com.facebook.AccessToken
import com.wanpaku.pochi.app.sign_in.view_models.SignInFormData
import com.wanpaku.pochi.app.sign_up.view_models.SignUpFormData
import com.wanpaku.pochi.domain.MailAddress
import com.wanpaku.pochi.domain.Password
import com.wanpaku.pochi.domain.repositories.AppUserRepository
import com.wanpaku.pochi.domain.user.User
import com.wanpaku.pochi.infra.api.request.toRequest
import com.wanpaku.pochi.infra.di.PerApplication
import com.wanpaku.pochi.infra.facebook.FacebookAuth
import com.wanpaku.pochi.infra.firebase.FirebaseAuthDelegate
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@PerApplication
class UserService @Inject constructor(private val userRepository: AppUserRepository) {

    fun createUserEmailAndPassword(email: MailAddress, password: Password, signUpFormData: SignUpFormData): Single<User> {
        return FirebaseAuthDelegate.createUser(email.value, password.value)
                .flatMap {
                    userRepository.create(signUpFormData.toRequest())
                            .subscribeOn(Schedulers.io())
                }
    }

    fun createUserFacebook(accessToken: AccessToken, signUpFormData: SignUpFormData): Single<User> {
        return FacebookAuth.login(accessToken)
                .flatMap {
                    userRepository.create(signUpFormData.toRequest())
                            .subscribeOn(Schedulers.io())
                }
    }

    fun loginEmailAndPassword(signInFormData: SignInFormData): Single<User> {
        return FirebaseAuthDelegate.signIn(signInFormData.email.value, signInFormData.password.value)
                .flatMap {
                    userRepository.login()
                            .subscribeOn(Schedulers.io())
                }
    }

    fun loginFacebook(accessToken: AccessToken): Single<User> {
        if (FacebookAuth.isLoggedIn()) FacebookAuth.logout()
        return FacebookAuth.login(accessToken)
                .flatMap {
                    userRepository.login()
                            .subscribeOn(Schedulers.io())
                }
    }

    fun accessToken() = FirebaseAuthDelegate.accessToken()

    fun isLoggedIn() = FirebaseAuthDelegate.isLoggedIn()

}