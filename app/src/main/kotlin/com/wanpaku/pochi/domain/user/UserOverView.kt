package com.wanpaku.pochi.domain.user

import paperparcel.PaperParcel
import paperparcel.PaperParcelable
import com.wanpaku.pochi.domain.user.PaperParcelUserOverView

@PaperParcel
data class UserOverView(val id: String,
                        val firstName: String,
                        val lastName: String,
                        val nickName: String,
                        val iconUri: String): PaperParcelable {

    companion object {

        @JvmField val CREATOR = PaperParcelUserOverView.CREATOR

    }

    fun fullName() = "$lastName $firstName"

}