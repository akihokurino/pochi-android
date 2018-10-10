package com.wanpaku.pochi.app.delegate

import android.content.Intent
import android.view.View
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.wanpaku.pochi.R
import com.wanpaku.pochi.app.internal.BaseFragment
import com.wanpaku.pochi.app.internal.ViewLifecycle
import com.wanpaku.pochi.infra.facebook.FacebookAuth

class FacebookLoginDelegate(private val facebookCallback: FacebookCallback<LoginResult>)
    : ViewLifecycle {

    private val facebookManager = CallbackManager.Factory.create()

    override fun onViewCreated(fragment: BaseFragment, source: View) {
        source.findViewById<LoginButton>(R.id.facebook_login).apply {
            setOnClickListener {
                // このボタンは登録用に使用するため、ログアウトを機能させない
                if (FacebookAuth.isLoggedIn()) FacebookAuth.logout()
            }
            setReadPermissions(listOf("email", "public_profile", "user_birthday"))
            this.fragment = fragment
            registerCallback(facebookManager, facebookCallback)
        }
    }

    override fun onActivityResult(fragment: BaseFragment, requestCode: Int, resultCode: Int, data: Intent?) {
        facebookManager.onActivityResult(requestCode, resultCode, data)
    }

}