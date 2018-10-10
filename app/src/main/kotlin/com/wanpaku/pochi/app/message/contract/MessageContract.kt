package com.wanpaku.pochi.app.message.contract

import android.net.Uri
import com.wanpaku.pochi.domain.booking.Booking
import com.wanpaku.pochi.domain.booking.Message
import com.wanpaku.pochi.domain.dog.Dog
import com.wanpaku.pochi.domain.sitter.Sitter
import org.joda.time.DateTime
import java.io.File

interface MessageContract {

    interface View {

        fun init(dogs: List<Dog>, sitter: com.wanpaku.pochi.domain.sitter.Sitter): Unit

        fun updateMessages(messages: List<Message>)

        fun updateNewMessage(message: Message)

        fun updateRequest(booking: Booking, dogs: List<Dog>, sitter: com.wanpaku.pochi.domain.sitter.Sitter)

        fun setIndicator(isVisible: Boolean): Unit

        interface User : View

        interface Sitter : View

    }

    interface Presenter {

        fun fetchMessages(bookingId: Long): Unit

        fun init(userId: String, sitterId: String): Unit

        fun sendMessage(bookingId: Long, fromUserId: String, message: String): Unit

        fun sendImage(bookingId: Long, file: File): Unit

        fun cancelRequest(bookingId: Long): Unit

        interface User {

            fun request(bookingId: Long, dogNames: List<String>, startDate: DateTime, endDate: DateTime, cardToken: String): Unit

        }

        interface Sitter {

            fun acceptRequest(bookingId: Long): Unit

        }

    }

}