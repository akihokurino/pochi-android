package com.wanpaku.pochi.app.sign_in.view_models

import android.text.TextUtils

data class ResetPasswordFormData(val email: String) {

    fun isValid(): Boolean {
        return !TextUtils.isEmpty(email)
    }

}