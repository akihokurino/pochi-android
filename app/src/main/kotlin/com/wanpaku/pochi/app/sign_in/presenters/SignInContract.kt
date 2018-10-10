package com.wanpaku.pochi.app.sign_in.presenters

import com.facebook.AccessToken
import com.wanpaku.pochi.app.sign_in.view_models.SignInFormData

interface SignInContract {

    interface View {

        fun launchSearch(): Unit

        fun setIndicator(isVisible: Boolean)

        fun showSignInError(message: String)

    }

    interface Presenter {

        fun loginEmailAndPassword(formData: SignInFormData): Unit

        fun loginFacebook(accessToken: AccessToken): Unit

    }

}