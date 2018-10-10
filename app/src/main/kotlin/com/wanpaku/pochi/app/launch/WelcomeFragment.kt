package com.wanpaku.pochi.app.launch

import android.net.Uri
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.OnClick
import com.facebook.*
import com.facebook.login.LoginResult
import com.wanpaku.pochi.R
import com.wanpaku.pochi.app.delegate.ButterKnifeBindDelegate
import com.wanpaku.pochi.app.delegate.FacebookLoginDelegate
import com.wanpaku.pochi.app.internal.BaseFragment
import com.wanpaku.pochi.app.internal.ViewLifecycle
import com.wanpaku.pochi.app.launch.presenters.WelcomeContract
import com.wanpaku.pochi.app.launch.presenters.WelcomePresenter
import com.wanpaku.pochi.app.maintab.MainTabActivity
import com.wanpaku.pochi.app.sign_in.SignInActivity
import com.wanpaku.pochi.app.sign_up.SignUpFormActivity
import com.wanpaku.pochi.infra.util.withLink
import javax.inject.Inject

class WelcomeFragment : BaseFragment(), WelcomeContract.View {

    companion object {

        fun newInstance() = WelcomeFragment()

    }

    @Inject lateinit var presenter: WelcomePresenter

    @BindView(R.id.attention) lateinit var attention: TextView

    override fun viewLifecycle(): List<ViewLifecycle> =
            listOf(presenter, ButterKnifeBindDelegate(this), FacebookLoginDelegate(object : FacebookCallback<LoginResult> {
                override fun onCancel() {
                    // TODO
                }

                override fun onError(error: FacebookException) {
                    // TODO
                }

                override fun onSuccess(result: LoginResult) {
                    presenter.onLoggedInFacebook(result.accessToken)
                }
            }))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_welcome, container, false).apply {
            isFocusableInTouchMode = true
            requestFocus()
        }
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAttention()
    }

    @OnClick(R.id.login)
    fun onLoginClicked() {
        SignInActivity.startActivity(context)
    }

    @OnClick(R.id.sign_up)
    fun onSignUpClicked() {
        SignUpFormActivity.startActivity(context)
    }

    @OnClick(R.id.skip)
    fun onSkipClicked() {
        MainTabActivity.startActivity(context)
    }

    private fun setupAttention() {
        attention.apply {
            // TODO URL置き換え
            val linkPair = mapOf(Pair("利用規約", "https://www.google.co.jp"),
                    Pair("プライバシーポリシー", "https://www.google.co.jp"))
            text = SpannableString(getString(R.string.attention_message)).withLink(linkPair, { title, url ->
                presenter.selectTextLink(title, url)
            }, ContextCompat.getColor(context, R.color.colorAccent))
            movementMethod = LinkMovementMethod.getInstance()
        }
    }

    override fun launchSignUpForFacebook(token: AccessToken, profile: Profile) {
        SignUpFormActivity.startActivity(context, token, profile)
    }

    override fun launchWebView(title: String, url: Uri) {
        // TODO
    }

}