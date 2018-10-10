package com.wanpaku.pochi.domain.booking

import com.wanpaku.pochi.domain.user.UserOverView
import com.wanpaku.pochi.domain.booking.PaperParcelMessage
import org.joda.time.DateTime
import paperparcel.PaperParcel
import paperparcel.PaperParcelable

@PaperParcel
data class Message(val bookingId: Long,
                   val id: Long,
                   val from: UserOverView?,
                   val type: Type,
                   val content: String?,
                   val imageUri: String?,
                   val createdAt: DateTime): PaperParcelable {

    companion object {

        @JvmField val CREATOR = PaperParcelMessage.CREATOR

    }

    enum class Type(val VALUE: String) {
        User("USER"),
        System("SYSTEM"),
    }

}