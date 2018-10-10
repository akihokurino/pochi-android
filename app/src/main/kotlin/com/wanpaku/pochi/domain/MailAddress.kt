package com.wanpaku.pochi.domain

import android.text.TextUtils
import timber.log.Timber

class MailAddress(val value: String) {

    init {
        check(Validator(value).isValid()) { Timber.e("invalid mail address : %s", value) }
    }

    class Validator(private val mailAddress: String) {

        private val MAIL_REGEX =
                "^[\\w!#%&'/=~`\\*\\+\\?\\{\\}\\^\\\$\\-\\|]+(\\.[\\w!#%&'/=~`\\*\\+\\?\\{\\}\\^\\\$\\-\\|]+)*@[\\w!#%&'/=~`\\*\\+\\?\\{\\}\\^\\\$\\-\\|]+(\\.[\\w!#%&'/=~`\\*\\+\\?\\{\\}\\^\\\$\\-\\|]+)*\$"

        fun isValid(): Boolean = !TextUtils.isEmpty(mailAddress) &&
                mailAddress.matches(Regex(MAIL_REGEX))

        fun to(): MailAddress? = if (isValid()) MailAddress(mailAddress) else null

    }

}