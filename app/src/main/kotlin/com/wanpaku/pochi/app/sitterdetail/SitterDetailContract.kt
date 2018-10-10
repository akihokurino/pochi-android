package com.wanpaku.pochi.app.sitterdetail

import com.wanpaku.pochi.domain.booking.Booking
import com.wanpaku.pochi.domain.dog.Dog
import com.wanpaku.pochi.domain.sitter.Review
import com.wanpaku.pochi.domain.sitter.Sitter

interface SitterDetailContract {

    interface View {

        fun update(sitter: Sitter, reviews: List<Review>, dogs: List<Dog>): Unit

        fun launchMessage(booking: Booking): Unit

        fun setIndicator(isVisible: Boolean): Unit

    }

    interface Presenter {

        fun fetchSitter(sitterId: String): Unit

        fun createBooking(sitterId: String): Unit

    }

}
