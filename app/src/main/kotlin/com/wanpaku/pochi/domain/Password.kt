package com.wanpaku.pochi.domain

import android.text.TextUtils
import timber.log.Timber

class Password(val value: String) {

    init {
        check(Validator(value).isValid()) { Timber.e("invalid password : %s", value) }
    }

    class Validator(private val password: String) {

        // Firebaseのパスワードは最低6文字必要
        // https://firebase.google.com/docs/reference/android/com/google/firebase/auth/FirebaseAuthWeakPasswordException
        private val PASSWORD_MINIMUM_NUMBER_OF_CHARACTERS = 6

        fun isValid(): Boolean = !TextUtils.isEmpty(password)
                && password.length >= PASSWORD_MINIMUM_NUMBER_OF_CHARACTERS

        fun to(): Password? = if (isValid()) Password(password) else null

    }

}