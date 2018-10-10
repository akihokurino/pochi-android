package com.wanpaku.pochi.app.booking.view

import com.wanpaku.pochi.domain.booking.Booking

enum class BookingCategory(val statuses: List<Booking.Status>) {
    Request(listOf(Booking.Status.PreRequest,
            Booking.Status.Request,
            Booking.Status.Refuse)),
    FixedRequest(listOf(Booking.Status.Confirm)),
    Stay(listOf(Booking.Status.Stay));

    companion object {

        fun find(index: Int) = values().find { it.ordinal == index }!!

    }

}