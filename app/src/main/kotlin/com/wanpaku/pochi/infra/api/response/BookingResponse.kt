package com.wanpaku.pochi.infra.api.response

import com.wanpaku.pochi.domain.booking.Booking
import com.wanpaku.pochi.domain.booking.Message
import com.wanpaku.pochi.infra.util.toDateTime
import proguard.annotation.KeepClassMemberNames

@KeepClassMemberNames
data class BookingResponse(val id: Long,
                           val user: UserOverViewResponse,
                           val sitter: UserOverViewResponse,
                           val dogIds: Set<Long>,
                           val status: String,
                           val startDate: String?,
                           val endDate: String?,
                           val days: Int?,
                           val unitPrice: Int,
                           val totalPrice: Int?,
                           val totalChargePrice: Int?,
                           val usePoint: Int?,
                           val maxRewardPoint: Int?,
                           val message: MessageResponse?,
                           val userReviewed: Boolean,
                           val sitterReviewed: Boolean,
                           val createdAt: Long,
                           val updatedAt: Long)

@KeepClassMemberNames
data class MessageResponse(val bookingId: Long,
                           val id: Long,
                           val from: UserOverViewResponse?,
                           val type: String,
                           val content: String?,
                           val imageUri: String?,
                           val createdAt: Long)

fun BookingResponse.to() = Booking(id = id,
        user = user.to(),
        sitter = sitter.to(),
        dogIds = dogIds,
        status = Booking.Status.values().find { it.VALUE == status }!!,
        startDate = startDate,
        endDate = endDate,
        days = days,
        unitPrice = unitPrice,
        totalPrice = totalPrice,
        totalChargePrice = totalChargePrice,
        usePoint = usePoint,
        maxRewardPoint = maxRewardPoint,
        message = message?.to(),
        userReviewed = userReviewed,
        sitterReviewed = sitterReviewed,
        updatedAt = updatedAt.toDateTime())

fun MessageResponse.to() = Message(bookingId = bookingId,
        id = id,
        from = from?.to(),
        type = Message.Type.values().find { it.VALUE == type }!!,
        content = content,
        imageUri = imageUri,
        createdAt = createdAt.toDateTime())