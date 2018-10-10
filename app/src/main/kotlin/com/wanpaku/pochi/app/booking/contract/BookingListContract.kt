package com.wanpaku.pochi.app.booking.contract

import com.wanpaku.pochi.app.booking.view.BookingCategory
import com.wanpaku.pochi.app.booking.view.BookingType
import com.wanpaku.pochi.domain.booking.Booking

interface BookingListContract {

    interface View {

        fun updateList(bookings: List<Booking>): Unit

        fun setIndicator(isVisible: Boolean): Unit

        fun showError(): Unit

    }

    interface Presenter {

        fun fetchBookings(bookingCategory: BookingCategory, bookingType: BookingType): Unit

    }

}