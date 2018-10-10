package com.wanpaku.pochi.domain.repositories

import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.securepreferences.SecurePreferences
import com.wanpaku.pochi.domain.user.User
import com.wanpaku.pochi.infra.api.PochiApiClient
import com.wanpaku.pochi.infra.api.request.CreateUserRequest
import com.wanpaku.pochi.infra.api.response.to
import com.wanpaku.pochi.infra.di.PerApplication
import io.reactivex.Single
import timber.log.Timber
import javax.inject.Inject

@PerApplication
class AppUserRepository @Inject constructor(private val apiClient: PochiApiClient,
                                            private val preferences: SecurePreferences) {

    private val PREFERENCE_KEY_APP_USER = "preference_key_app_user"

    fun create(userRequest: CreateUserRequest): Single<User> {
        return apiClient.createuser(userRequest)
                .map { it.to() }
                .doOnSuccess { store(it) }
    }

    fun login(): Single<User> {
        return apiClient.me().map { it.to() }
                .doOnSuccess { store(it) }
    }

    // TODO とりあえず保存　どうあるべきかは別で検討
    fun store(user: User) {
        val json = Gson().toJson(user)
        preferences.edit().putString(PREFERENCE_KEY_APP_USER, json).apply()
    }

    fun restore(): User? {
        val json = preferences.getString(PREFERENCE_KEY_APP_USER, "")
        if (TextUtils.isEmpty(json)) {
            Timber.d("User does not exist")
            return null
        }
        try {
            val appUser = Gson().fromJson(json, User::class.java)
            return appUser
        } catch (e: JsonSyntaxException) {
            Timber.e(e, "failed to restore User")
            return null
        }
    }

    fun isLoggedIn(): Boolean = restore() != null

}