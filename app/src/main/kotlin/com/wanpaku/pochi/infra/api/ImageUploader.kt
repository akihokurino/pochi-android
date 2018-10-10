package com.wanpaku.pochi.infra.api

import com.wanpaku.pochi.infra.di.PerApplication
import io.reactivex.Single
import okhttp3.*
import java.io.File
import java.io.IOException
import javax.inject.Inject

@PerApplication
class ImageUploader @Inject constructor(private val client: OkHttpClient) {

    fun upload(file: File, url: String): Single<Response> {
        val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", file.name, RequestBody
                        .create(MediaType.parse("image/jpg"), file))
                .build()
        val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()
        return Single.create {
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    it.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    it.onSuccess(response)
                }
            })
        }
    }

}