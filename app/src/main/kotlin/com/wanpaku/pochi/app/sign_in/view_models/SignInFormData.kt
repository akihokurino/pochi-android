package com.wanpaku.pochi.app.sign_in.view_models

import com.wanpaku.pochi.domain.MailAddress
import com.wanpaku.pochi.domain.Password

data class SignInFormData(val email: MailAddress,
                          val password: Password) {

    class Validator(private val email: String,
                    private val password: String) {

        fun isValid(): Boolean = MailAddress.Validator(email).isValid()
                && Password.Validator(password).isValid()

        fun to(): SignInFormData? = if (isValid()) SignInFormData(MailAddress(email), Password(password)) else null

    }

}