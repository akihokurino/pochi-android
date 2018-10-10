package com.wanpaku.pochi.domain.repositories

import android.net.Uri
import com.google.gson.Gson
import com.wanpaku.pochi.domain.booking.Message
import com.wanpaku.pochi.infra.api.ImageUploader
import com.wanpaku.pochi.infra.api.PochiApiClient
import com.wanpaku.pochi.infra.api.request.CreateMessageRequest
import com.wanpaku.pochi.infra.api.response.MessageResponse
import com.wanpaku.pochi.infra.api.response.to
import com.wanpaku.pochi.infra.di.PerApplication
import io.reactivex.Single
import java.io.File
import javax.inject.Inject

@PerApplication
class MessageRepository @Inject constructor(private val client: PochiApiClient,
                                            private val imageUploader: ImageUploader,
                                            private val gson: Gson) {

    fun postMessage(bookingId: Long, request: CreateMessageRequest) = client
            .postMessage(bookingId, request)
            .map { it.to() }

    fun fetchMessages(bookingId: Long, cursor: String? = null) = client
            .fetchMessages(bookingId, cursor)
            .map { Pair(it.nextCursor, it.items.map { it.to() }) }

    fun uploadImageUrl(bookingId: Long) = client
            .fetchUploadImageUrl(bookingId)
            .map { it.url }

    fun uploadImage(file: File, url: String): Single<Message> {
        return imageUploader.upload(file, url)
                .map { gson.fromJson(it.body()!!.string(), MessageResponse::class.java) }
                .map { it.to() }
    }

}