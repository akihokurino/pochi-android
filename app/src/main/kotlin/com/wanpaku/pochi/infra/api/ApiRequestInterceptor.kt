package com.wanpaku.pochi.infra.api

import com.wanpaku.pochi.infra.firebase.FirebaseAuthDelegate
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class ApiRequestInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
                .addHeader("User-Agent", "pochi_android/1.0")
                .addHeader("X-Pochi-Client", "POCHI_ANDROID")

        val request = accessToken()?.let {
            builder.addHeader("Authorization", "Bearer $it").build()
        } ?: builder.build()

        return chain.proceed(request)
    }

    private fun accessToken(): String? = FirebaseAuthDelegate.accessToken()

}