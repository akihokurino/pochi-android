package com.wanpaku.pochi.infra.facebook

import com.facebook.AccessToken
import com.facebook.Profile
import com.facebook.ProfileTracker
import com.facebook.login.LoginManager
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseUser
import com.wanpaku.pochi.infra.firebase.FirebaseAuthDelegate
import io.reactivex.Single

object FacebookAuth {

    fun login(accessToken: AccessToken): Single<FirebaseUser> {
        val credential = FacebookAuthProvider.getCredential(accessToken.token)
        return FirebaseAuthDelegate.signInWithCredential(credential)
    }

    fun isLoggedIn() = AccessToken.getCurrentAccessToken() != null

    fun logout() = LoginManager.getInstance().logOut()

    fun profileWithTracking(): Single<Profile> {
        return Single.create { subscriber ->
            // ログイン直後でもgetCurrentProfileがnullの場合があるため、ProfileTrackerでProfileが取得できるのを待つ
            val profile = Profile.getCurrentProfile()
            if (profile != null) {
                subscriber.onSuccess(profile)
                return@create
            }
            object : ProfileTracker() {
                override fun onCurrentProfileChanged(oldProfile: Profile?, currentProfile: Profile) {
                    subscriber.onSuccess(currentProfile)
                    stopTracking()
                }
            }
        }
    }

}
