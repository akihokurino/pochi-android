package com.wanpaku.pochi.app.sign_up.presenters

import com.facebook.AccessToken
import com.wanpaku.pochi.app.sign_up.FacebookLoginData
import com.wanpaku.pochi.app.sign_up.view_models.SignUpFormData

interface SignUpFormContract {

    interface View {

        fun setIndicator(isVisible: Boolean): Unit

        fun launchDogRegistration(): Unit

        fun showError(message: String): Unit

    }

    interface Presenter {

        fun createUserWithEmailAndPassword(formData: SignUpFormData): Unit

        fun createUserWithFacebook(accessToken: AccessToken, formData: SignUpFormData): Unit

    }

}