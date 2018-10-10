package com.wanpaku.pochi.domain.repositories

import com.wanpaku.pochi.infra.api.PochiApiClient
import com.wanpaku.pochi.infra.api.request.RequestBookingRequest
import com.wanpaku.pochi.infra.api.request.UpdateBookingStatusRequest
import com.wanpaku.pochi.infra.api.response.to
import javax.inject.Inject

class BookingRepository @Inject constructor(private val client: PochiApiClient) {

    fun fetchUserBookings(userId: String) = client
            .fetchUserBookings(userId)
            .map { Pair(it.nextCursor, it.items.map { it.to() }) }

    fun fetchSitterBookings(sitterId: String) = client
            .fetchSitterBookings(sitterId)
            .map { Pair(it.nextCursor, it.items.map { it.to() }) }

    fun createBooking(sitterId: String) = client
            .createBooking(sitterId)
            .map { it.to() }

    fun requestBooking(bookingId: Long, request: RequestBookingRequest) = client
            .requestBooking(bookingId, request)
            .map { it.to() }

    fun updateBookingStatus(bookingId: Long, request: UpdateBookingStatusRequest) = client
            .updateBookingStatus(bookingId, request)
            .map { it.to() }

}