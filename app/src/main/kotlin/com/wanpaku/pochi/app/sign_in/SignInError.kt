package com.wanpaku.pochi.app.sign_in

import android.support.annotation.StringRes
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.wanpaku.pochi.R

enum class SignInError(@StringRes val messageResId: Int) {
    InvalidUser(R.string.email_does_not_exist),
    InvalidCredentials(R.string.email_address_is_malformed_message),
    Unknown(R.string.failed_to_sign_in_try_again);

    companion object {

        fun from(throwable: Throwable): SignInError =
                when (throwable) {
                    is FirebaseAuthInvalidUserException -> SignInError.InvalidUser
                    is FirebaseAuthInvalidCredentialsException -> SignInError.InvalidCredentials
                    else -> SignInError.Unknown
                }

    }

}