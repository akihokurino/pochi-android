package com.wanpaku.pochi.domain.booking

import android.content.Context
import com.wanpaku.pochi.R
import com.wanpaku.pochi.domain.booking.PaperParcelBooking
import com.wanpaku.pochi.domain.user.UserOverView
import org.joda.time.DateTime
import paperparcel.PaperParcel
import paperparcel.PaperParcelable

@PaperParcel
data class Booking(val id: Long,
                   val user: UserOverView,
                   val sitter: UserOverView,
                   val dogIds: Set<Long>,
                   val status: Status,
                   val startDate: String?,
                   val endDate: String?,
                   val days: Int?,
                   val unitPrice: Int,
                   val totalPrice: Int?,
                   val totalChargePrice: Int?,
                   val usePoint: Int?,
                   val maxRewardPoint: Int?,
                   val message: Message?,
                   val userReviewed: Boolean,
                   val sitterReviewed: Boolean,
                   val updatedAt: DateTime): PaperParcelable {

    companion object {

        @JvmField val CREATOR = PaperParcelBooking.CREATOR

    }

    enum class Status(val VALUE: String) {
        PreRequest("PREREQUEST"),
        Request("REQUEST"),
        Refuse("REFUSE"),
        Confirm("CONFIRM"),
        Cancel("CANCEL"),
        Stay("STAY"),
        Completion("COMPLETION"),
        Close("CLOSE"),
    }

    fun period(context: Context): String {
        val startDate = startDate
        val endDate = endDate
        return when {
            startDate != null && endDate != null -> context.getString(R.string.period, startDate, endDate)
            else -> context.getString(R.string.period_undecided)
        }
    }

}