package com.wanpaku.pochi.domain.user

import paperparcel.PaperParcel
import paperparcel.PaperParcelable

@PaperParcel
data class User(val id: String,
                val firstName: String,
                val lastName: String,
                val nickName: String,
                val iconUri: String,
                val introductionMessage: String,
                val point: Long,
                val isNotificationOptedIn: Boolean): PaperParcelable {

    companion object {
        @JvmField val CREATOR = PaperParcelUser.CREATOR
    }

    fun fullName() = "$firstName $lastName"

}