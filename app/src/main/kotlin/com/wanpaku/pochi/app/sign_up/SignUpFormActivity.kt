package com.wanpaku.pochi.app.sign_up

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.facebook.AccessToken
import com.facebook.Profile
import com.wanpaku.pochi.R
import com.wanpaku.pochi.app.delegate.ToolbarDelegate
import com.wanpaku.pochi.app.internal.BaseActivity

class SignUpFormActivity : BaseActivity() {

    companion object {

        private val KEY_FACEBOOK_LOGIN_DATA = "key_facebook_login_data"

        fun startActivity(context: Context, accessToken: AccessToken? = null, facebookProfile: Profile? = null) {
            val intent = Intent(context, SignUpFormActivity::class.java)
            if (accessToken != null && facebookProfile != null) {
                val data = FacebookLoginData(accessToken, facebookProfile)
                intent.putExtra(KEY_FACEBOOK_LOGIN_DATA, data)
            }
            context.startActivity(intent)
        }

        private fun facebookLoginData(intent: Intent): FacebookLoginData? {
            if (intent.hasExtra(KEY_FACEBOOK_LOGIN_DATA)) return intent
                    .getParcelableExtra<FacebookLoginData>(KEY_FACEBOOK_LOGIN_DATA)
            return null
        }

    }

    override fun lifecycleDelegates() = listOf(
            ToolbarDelegate(title = getString(R.string.activity_sign_up_human_title),
                    displayHomeAsUpEnabled = true)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_form)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, SignUpFormFragment.newInstance(facebookLoginData(intent)))
                    .commit()
        }

    }

}