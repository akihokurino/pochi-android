package com.wanpaku.pochi.app.sign_up.view_models

import com.wanpaku.pochi.domain.MailAddress
import com.wanpaku.pochi.domain.Password

class SignUpFormData private constructor(val lastName: String,
                                         val firstName: String,
                                         val nickName: String,
                                         val email: MailAddress?,
                                         val password: Password?) {

    class ValidatorFactory(private val isFacebook: Boolean) {

        fun create(lastName: String = "",
                   firstName: String = "",
                   nickName: String = "",
                   email: String = "",
                   password: String = ""): Validator {
            return when {
                isFacebook -> FacebookValidator(lastName = lastName,
                        firstName = firstName,
                        nickName = nickName)
                else -> DefaultValidator(lastName = lastName,
                        firstName = firstName,
                        nickName = nickName,
                        email = email,
                        password = password)
            }
        }

    }

    interface Validator {

        val lastName: String
        val firstName: String
        val nickName: String
        val email: String?
        val password: String?

        fun isValidLastName(): Boolean

        fun isValidFirstName(): Boolean

        fun isValidNickName(): Boolean

        fun isValidEmail(): Boolean

        fun isValidPassword(): Boolean

        fun isValid(): Boolean

        fun to(): SignUpFormData?

    }

    open class DefaultValidator(override val lastName: String,
                                override val firstName: String,
                                override val nickName: String,
                                override val email: String?,
                                override val password: String?)
        : SignUpFormData.Validator {

        private val MAX_LAST_NAME_LENGTH = 16
        private val MAX_FIRST_NAME_LENGTH = 16
        private val MAX_NICK_NAME_LENGTH = 16

        override fun isValidLastName() = !lastName.isNullOrEmpty()
                && lastName.length <= MAX_LAST_NAME_LENGTH

        override fun isValidFirstName() = !firstName.isNullOrEmpty()
                && firstName.length <= MAX_FIRST_NAME_LENGTH

        override fun isValidNickName() = !nickName.isNullOrEmpty()
                && nickName.length <= MAX_NICK_NAME_LENGTH

        override fun isValidEmail() = !email.isNullOrEmpty() && MailAddress
                .Validator(email!!).isValid()

        override fun isValidPassword() = Password
                .Validator(password!!).isValid()

        override fun isValid() = isValidLastName()
                && isValidFirstName()
                && isValidNickName()
                && isValidEmail()
                && isValidPassword()

        override fun to(): SignUpFormData? {
            if (isValid()) return SignUpFormData(lastName = lastName,
                    firstName = firstName,
                    nickName = nickName,
                    email = email?.let(::MailAddress),
                    password = password?.let(::Password))
            return null
        }

    }

    class FacebookValidator(lastName: String,
                            firstName: String,
                            nickName: String)
        : DefaultValidator(lastName, firstName, nickName, null, null) {

        // Facebookログインではメールアドレスとパスワードは不要なため問題ないとしてtrueを返す
        override fun isValidEmail() = true

        override fun isValidPassword() = true

    }

}