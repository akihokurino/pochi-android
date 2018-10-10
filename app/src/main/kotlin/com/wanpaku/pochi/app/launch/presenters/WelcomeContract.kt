package com.wanpaku.pochi.app.launch.presenters

import android.net.Uri
import com.facebook.AccessToken
import com.facebook.Profile

interface WelcomeContract {

    interface View {

        fun launchSignUpForFacebook(accessToken: AccessToken, profile: Profile): Unit

        fun launchWebView(title: String, url: Uri): Unit

    }

    interface Presenter {

        fun selectTextLink(title: String, url: String): Unit

        fun onLoggedInFacebook(accessToken: AccessToken): Unit

    }

}