package com.wanpaku.pochi.infra.api.response

import com.wanpaku.pochi.domain.user.User
import com.wanpaku.pochi.domain.user.UserOverView
import proguard.annotation.KeepClassMemberNames

@KeepClassMemberNames
data class UserResponse(val id: String,
                        val firstName: String,
                        val lastName: String,
                        val nickname: String,
                        val iconUri: String,
                        val introductionMessage: String,
                        val point: Long,
                        val createdAt: Long,
                        val updatedAt: Long,
                        val accessToken: String,
                        val isNotificationOptedIn: Boolean)

@KeepClassMemberNames
data class UserOverViewResponse(val id: String,
                                val firstName: String,
                                val lastName: String,
                                val nickname: String,
                                val iconUri: String)

fun UserResponse.to(): User
        = User(id = id,
        firstName = firstName,
        lastName = lastName,
        nickName = nickname,
        iconUri = iconUri,
        introductionMessage = introductionMessage,
        point = point,
        isNotificationOptedIn = isNotificationOptedIn)

fun UserOverViewResponse.to() = UserOverView(id = id,
        firstName = firstName,
        lastName = lastName,
        nickName = nickname,
        iconUri = iconUri)