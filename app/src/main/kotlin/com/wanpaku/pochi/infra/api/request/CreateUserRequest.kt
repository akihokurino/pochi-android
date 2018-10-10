package com.wanpaku.pochi.infra.api.request

import com.wanpaku.pochi.app.sign_up.view_models.SignUpFormData
import proguard.annotation.KeepClassMemberNames

@KeepClassMemberNames
data class CreateUserRequest(val firstName: String,
                             val lastName: String,
                             val nickname: String)

fun SignUpFormData.toRequest()
        = CreateUserRequest(firstName = firstName,
        lastName = lastName,
        nickname = nickName)