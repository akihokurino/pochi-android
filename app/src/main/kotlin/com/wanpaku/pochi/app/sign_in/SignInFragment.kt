package com.wanpaku.pochi.app.sign_in

import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import butterknife.BindView
import butterknife.OnClick
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.jakewharton.rxbinding2.widget.RxTextView
import com.wanpaku.pochi.R
import com.wanpaku.pochi.app.delegate.ButterKnifeBindDelegate
import com.wanpaku.pochi.app.delegate.FacebookLoginDelegate
import com.wanpaku.pochi.app.internal.BaseFragment
import com.wanpaku.pochi.app.maintab.MainTabActivity
import com.wanpaku.pochi.app.sign_in.presenters.SignInContract
import com.wanpaku.pochi.app.sign_in.presenters.SignInFormPresenter
import com.wanpaku.pochi.app.sign_in.view_models.SignInFormData
import com.wanpaku.pochi.domain.MailAddress
import com.wanpaku.pochi.domain.Password
import com.wanpaku.pochi.infra.ui.widget.TwoStateButton
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class SignInFragment : BaseFragment(), SignInContract.View {

    companion object {

        fun newInstance() = SignInFragment()

    }

    @BindView(R.id.progress_bar) lateinit var progress: ProgressBar
    @BindView(R.id.loginBtn) lateinit var login: TwoStateButton
    @BindView(R.id.mail_address) lateinit var mailAddressTextInputLayout: TextInputLayout
    @BindView(R.id.mail_address_edittext) lateinit var mailAddressEditText: EditText
    @BindView(R.id.password) lateinit var passwordTextInputLayout: TextInputLayout
    @BindView(R.id.password_edittext) lateinit var passwordEditText: EditText

    @Inject lateinit var presenter: SignInFormPresenter

    private val disposables = CompositeDisposable()

    override fun viewLifecycle() = listOf(presenter, ButterKnifeBindDelegate(this), FacebookLoginDelegate(object : FacebookCallback<LoginResult> {

        override fun onError(error: FacebookException) {
            // TODO
        }

        override fun onSuccess(result: LoginResult) {
            presenter.loginFacebook(result.accessToken)
        }

        override fun onCancel() {
            // TODO
        }

    }))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_sign_in, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mailAddressChangedObservable = RxTextView
                .textChanges(mailAddressEditText).cache()
        val passwordChangedObservable = RxTextView
                .textChanges(passwordEditText).cache()
        setupMailAddressErrorObservable(mailAddressChangedObservable)
        setupPasswordErrorObservable(passwordChangedObservable)
        setupFormValidationObservable(mailAddressChangedObservable, passwordChangedObservable)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposables.clear()
    }

    @OnClick(R.id.loginBtn)
    fun onLoginClicked() {
        formData()?.let { presenter.loginEmailAndPassword(it) }
    }

    @OnClick(R.id.forgetPasswordBtn)
    fun onResetPasswordClicked() {
        // TODO
    }

    override fun launchSearch() {
        MainTabActivity.startActivity(context)
    }

    override fun showSignInError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun setIndicator(isVisible: Boolean) {
        progress.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun setupMailAddressErrorObservable(mailAddressChangedObservable: Observable<CharSequence>) {
        disposables.add(mailAddressChangedObservable
                .map { !MailAddress.Validator(it.toString()).isValid() && it.isNotEmpty() }
                .subscribe { isErrorEnabled ->
                    mailAddressTextInputLayout.isErrorEnabled = isErrorEnabled
                    if (isErrorEnabled) mailAddressTextInputLayout.error =
                            getString(R.string.email_validation_error_message)
                })
    }

    private fun setupPasswordErrorObservable(passwordChangedObservable: Observable<CharSequence>) {
        disposables.add(passwordChangedObservable
                .map { !Password.Validator(it.toString()).isValid() && it.isNotEmpty() }
                .subscribe { isErrorEnabled ->
                    passwordTextInputLayout.isErrorEnabled = isErrorEnabled
                    if (isErrorEnabled) passwordTextInputLayout.error =
                            getString(R.string.password_validation_error_message)
                })
    }

    private fun setupFormValidationObservable(mailAddressChangedObservable: Observable<CharSequence>,
                                              passwordChangedObservable: Observable<CharSequence>) {
        disposables.add(Observable
                .combineLatest<CharSequence, CharSequence, SignInFormData.Validator>(
                        mailAddressChangedObservable,
                        passwordChangedObservable,
                        BiFunction { e, p -> SignInFormData.Validator(e.toString(), p.toString()) })
                .subscribe { login.isActive = it.isValid() })
    }

    private fun formData(): SignInFormData? = SignInFormData
            .Validator(email = mailAddressEditText.text.toString(),
                    password = passwordEditText.text.toString()).to()

}