package com.wanpaku.pochi.app.sign_up

import android.content.Context
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import com.jakewharton.rxbinding2.widget.RxTextView
import com.wanpaku.pochi.R
import com.wanpaku.pochi.app.dog_registration.DogRegistrationActivity
import com.wanpaku.pochi.app.internal.BaseFragment
import com.wanpaku.pochi.app.internal.ViewLifecycle
import com.wanpaku.pochi.app.sign_up.presenters.SignUpFormContract
import com.wanpaku.pochi.app.sign_up.presenters.SignUpFormPresenter
import com.wanpaku.pochi.app.sign_up.view_models.SignUpFormData
import com.wanpaku.pochi.infra.ui.widget.TwoStateButton
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function5
import javax.inject.Inject

class SignUpFormFragment : BaseFragment(), SignUpFormContract.View {

    companion object {

        private val KEY_FACEBOOK_LOGIN_DATA = "key_facebook_login_data"

        fun newInstance(facebookLoginData: FacebookLoginData?) = SignUpFormFragment().apply {
            arguments = Bundle().apply {
                if (facebookLoginData != null) putParcelable(KEY_FACEBOOK_LOGIN_DATA, facebookLoginData)
            }
        }

        private fun facebookLoginData(bundle: Bundle): FacebookLoginData? {
            if (bundle.containsKey(KEY_FACEBOOK_LOGIN_DATA)) return bundle
                    .getParcelable(KEY_FACEBOOK_LOGIN_DATA)
            return null
        }

    }

    @Inject lateinit var presenter: SignUpFormPresenter

    private val viewDelegate by lazy {
        SignUpFormViewDelegate(presenter, context, facebookLoginData(arguments))
    }

    private val disposable = CompositeDisposable()

    override fun viewLifecycle(): List<ViewLifecycle> = listOf(presenter, viewDelegate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sign_up_form, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposable.clear()
    }

    override fun setIndicator(isVisible: Boolean) {
        viewDelegate.progress.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    override fun launchDogRegistration() {
        DogRegistrationActivity.startActivity(context)
    }

    override fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

}

// 一旦
class SignUpFormViewDelegate(private val presenter: SignUpFormPresenter,
                             private val context: Context,
                             private val facebookLoginData: FacebookLoginData?)
    : ViewLifecycle {

    private val validatorFactory = SignUpFormData.ValidatorFactory(facebookLoginData != null)

    @BindView(R.id.next) lateinit var nextButton: TwoStateButton
    @BindView(R.id.progress_bar) lateinit var progress: ProgressBar
    @BindView(R.id.last_name_edittext) lateinit var lastNameEditText: EditText
    @BindView(R.id.first_name_edittext) lateinit var firstNameEditText: EditText
    @BindView(R.id.nickname_edittext) lateinit var nicknameEditText: EditText
    @BindView(R.id.mail_address_edittext) lateinit var mailAddressEditText: EditText
    @BindView(R.id.mail_address) lateinit var mailAddressTextInputLayout: TextInputLayout
    @BindView(R.id.password_edittext) lateinit var passwordEditText: EditText
    @BindView(R.id.password) lateinit var passwordTextInputLayout: TextInputLayout

    private val disposables = CompositeDisposable()
    private var binder: Unbinder? = null

    private var formData: SignUpFormData? = null

    override fun onViewCreated(fragment: BaseFragment, source: View) {
        binder = ButterKnife.bind(this, source)
        setupView()
        val mailAddressObservable = RxTextView
                .textChanges(mailAddressEditText).cache()
        val passwordObservable = RxTextView
                .textChanges(passwordEditText).cache()
        setupFormValidationObservable(mailAddressObservable, passwordObservable)
        setupMailAddressErrorObservable(mailAddressObservable)
        setupPasswordErrorObservable(passwordObservable)
    }

    override fun onDestroyView(fragment: BaseFragment) {
        disposables.clear()
        binder?.unbind()
    }

    @OnClick(R.id.next)
    fun onNextClicked() {
        formData?.let {
            when {
                facebookLoginData != null -> presenter
                        .createUserWithFacebook(facebookLoginData.token, it)
                else -> presenter.createUserWithEmailAndPassword(it)
            }
        }
    }

    private fun setupView() {
        val profile = facebookLoginData?.profile ?: return
        lastNameEditText.setText(profile.lastName)
        firstNameEditText.setText(profile.firstName)
        nicknameEditText.setText(profile.middleName)
        // Facebook登録時はメールアドレスとパスワードは不要なので非表示にする
        mailAddressTextInputLayout.visibility = View.GONE
        passwordTextInputLayout.visibility = View.GONE
    }

    private fun setupFormValidationObservable(mailAddressChangedObservable: Observable<CharSequence>,
                                              passwordChangedObservable: Observable<CharSequence>) {
        val formObservable: Observable<SignUpFormData.Validator> = Observable.combineLatest(
                RxTextView.textChanges(lastNameEditText),
                RxTextView.textChanges(firstNameEditText),
                RxTextView.textChanges(nicknameEditText),
                mailAddressChangedObservable,
                passwordChangedObservable,
                Function5 { lastName, firstName, nickName, mailAddress, password ->
                    validatorFactory.create(lastName = lastName.toString(),
                            firstName = firstName.toString(),
                            nickName = nickName.toString(),
                            email = mailAddress.toString(),
                            password = password.toString())
                })

        disposables.add(formObservable
                .subscribe {
                    formData = it.to()
                    nextButton.isActive = it.isValid()
                })
    }

    private fun setupMailAddressErrorObservable(mailAddressChangedObservable: Observable<CharSequence>) {
        disposables.add(mailAddressChangedObservable
                .map { !validatorFactory.create(email = it.toString()).isValidEmail() && it.isNotEmpty() }
                .subscribe { isErrorEnabled ->
                    mailAddressTextInputLayout.isErrorEnabled = isErrorEnabled
                    if (isErrorEnabled) mailAddressTextInputLayout.error = context.getString(R.string.email_validation_error_message)
                })
    }

    private fun setupPasswordErrorObservable(passwordChangedObservable: Observable<CharSequence>) {
        disposables.add(passwordChangedObservable
                .map { !validatorFactory.create(password = it.toString()).isValidPassword() && it.isNotEmpty() }
                .subscribe { isErrorEnabled ->
                    passwordTextInputLayout.isErrorEnabled = isErrorEnabled
                    if (isErrorEnabled) passwordTextInputLayout.error = context.getString(R.string.password_validation_error_message)
                })
    }


}