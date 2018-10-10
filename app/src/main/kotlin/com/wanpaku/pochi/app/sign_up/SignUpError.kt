package com.wanpaku.pochi.app.sign_up

import android.support.annotation.StringRes
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.wanpaku.pochi.R

enum class SignUpError(@StringRes val messageResId: Int) {
    WeakPassword(R.string.password_is_not_strong_enough),
    InvalidCredentials(R.string.email_address_is_malformed_message),
    UserCollision(R.string.already_exists_an_account_with_the_given_email_address),
    Unknown(R.string.failed_to_sign_up_try_again);

    companion object {

        fun from(throwable: Throwable): SignUpError =
                when (throwable) {
                    is FirebaseAuthWeakPasswordException -> WeakPassword
                    is FirebaseAuthInvalidCredentialsException -> InvalidCredentials
                    is FirebaseAuthUserCollisionException -> UserCollision
                    else -> Unknown
                }

    }

}
