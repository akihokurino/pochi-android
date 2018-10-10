package com.wanpaku.pochi.app.sign_up

import com.facebook.AccessToken
import com.facebook.Profile
import paperparcel.PaperParcel
import paperparcel.PaperParcelable

@PaperParcel
data class FacebookLoginData(val token: AccessToken,
                             val profile: Profile) : PaperParcelable {

    companion object {
        @JvmField val CREATOR = PaperParcelFacebookLoginData.CREATOR
    }

}
