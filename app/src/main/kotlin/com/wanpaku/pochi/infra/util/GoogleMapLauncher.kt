package com.wanpaku.pochi.infra.util

import android.content.Context
import android.content.Intent
import android.net.Uri


object GoogleMapLauncher {

    fun laucnh(latitude: Double, longitude: Double, context: Context) {
        val uri = Uri.parse("geo:$latitude,$longitude")
        val intent = Intent(Intent.ACTION_VIEW, uri).apply {
            `package` = "com.google.android.apps.maps"
        }
        if (intent.resolveActivity(context.packageManager) == null) return
        context.startActivity(intent)
    }

}
